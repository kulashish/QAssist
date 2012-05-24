package com.aneedo.search.ranking.rgcd;

import java.util.ArrayList;
import java.util.Collection;

import org.ejml.data.DenseMatrix64F;
import org.ejml.simple.SimpleMatrix;
import org.ojalgo.function.aggregator.AggregatorFunction;
import org.ojalgo.matrix.store.MatrixStore;
import org.ojalgo.matrix.store.PhysicalStore;
import org.ojalgo.matrix.store.PrimitiveDenseStore;
import org.ojalgo.optimisation.OptimisationSolver.Result;
import org.ojalgo.optimisation.Variable;
import org.ojalgo.optimisation.quadratic.QuadraticExpressionsModel;
import org.ojalgo.optimisation.quadratic.QuadraticSolver;
import org.ojalgo.scalar.Scalar;

public class LeastSquareTrainerQP
{
	private DenseMatrix64F lambdas = null;

	private SimpleMatrix[][] featureMatrix;
	private SimpleMatrix[] optimumTeleport;
	private SimpleMatrix[] relevance;

	public DenseMatrix64F getLambdas()
	{
		return lambdas;
	}

	/**
	 * 
	 * @param featureMatrix
	 *            Mf Matrices for all queries
	 * @param optimumTeleport
	 *            r* Matrices for all queries
	 * @param relevance
	 *            b Matrices for all queries
	 * 
	 */
	public void train(SimpleMatrix[][] featureMatrix, SimpleMatrix[] optimumTeleport, SimpleMatrix[] relevance)
	{
		this.featureMatrix = featureMatrix;
		this.optimumTeleport = optimumTeleport;
		this.relevance = relevance;

		QuadraticExpressionsModel model = new QuadraticExpressionsModel(variables());
		QuadraticSolver.Builder builder = new QuadraticSolver.Builder(model);
		builder.objective(Q(), C());
		builder.equalities(AE(), BE());
		builder.inequalities(AI(), BI());
		QuadraticSolver qp = builder.build();
		Result result = qp.solve();
		finalizeLambdas(result);
	}

	private Collection<Variable> variables()
	{
		ArrayList<Variable> variables = new ArrayList<Variable>(numFeatures() + 1); // 1 for Lambda0
		for (int i = 0; i <= numFeatures(); i++)
		{
			variables.add(new Variable("Lambda" + i));
		}
		return variables;
	}

	private MatrixStore<Double> Q()
	{
		int n = numFeatures() + 1;
		SimpleMatrix qm = new SimpleMatrix(n, n);
		qm.zero();
		int totalIntrptns = countTotalNumberOfInterpretations();
		DenseMatrix64F b = new DenseMatrix64F(numQueries() * totalIntrptns, 1);
		DenseMatrix64F coeffMatrix = new DenseMatrix64F(numQueries() * totalIntrptns, numFeatures());
		for (int q = 0; q < numQueries(); q++)
		{
			for (int i = 0; i < numFeatures(); i++)
			{
				SimpleMatrix mf = featureMatrix[q][i].mult(optimumTeleport[q]);

				// Fill the coefficient matrix
				for (int r = 0; r < mf.numRows(); r++)
				{
					coeffMatrix.set(q * numInterpretations(q) + r, i, mf.get(r, 0));
				}
			}

			for (int r = 0; r < relevance[q].numRows(); r++)
			{
				b.set(q * numInterpretations(q) + r, 0, relevance[q].get(r, 0));
			}
		}

		// Compute sum of squared errors
		for (int r = 0; r < coeffMatrix.getNumRows(); r++)
		{
			double[] coeffRow = new double[coeffMatrix.getNumCols() + 1];
			for (int c = 0; c < coeffMatrix.getNumCols(); c++)
				coeffRow[c] = coeffMatrix.get(r, c);
			coeffRow[coeffRow.length - 1] = -1.0 * b.get(r, 0);
			qm = qm.plus(prepareSquaredMatrix(coeffRow));
		}

		PrimitiveDenseStore q = PrimitiveDenseStore.FACTORY.makeZero(n, n);
		for (int r = 0; r < qm.numRows(); r++)
			for (int c = 0; c < qm.numCols(); c++)
				q.set(r, c, qm.get(r, c));

		return q;
	}

	private SimpleMatrix prepareSquaredMatrix(double[] coeffRow)
	{
		SimpleMatrix qm = new SimpleMatrix(coeffRow.length, coeffRow.length);

		for (int r = 0; r < coeffRow.length; r++)
			for (int c = 0; c < coeffRow.length; c++)
				qm.set(r, c, coeffRow[r] * coeffRow[c]);

		return qm;
	}

	private MatrixStore<Double> C()
	{
		PrimitiveDenseStore c = PrimitiveDenseStore.FACTORY.makeZero(numFeatures() + 1, 1);
		return c;
	}

	private MatrixStore<Double> AE()
	{
		int n = numFeatures() + 1;
		PrimitiveDenseStore ae = PrimitiveDenseStore.FACTORY.makeZero(n, n);
		ae.set(n - 1, n - 1, 1);
		return ae;
	}

	private MatrixStore<Double> BE()
	{
		int n = numFeatures() + 1;
		PrimitiveDenseStore be = PrimitiveDenseStore.FACTORY.makeZero(n, 1);
		be.set(n - 1, 0, 1);
		return be;
	}

	private MatrixStore<Double> AI()
	{
		int n = numFeatures() + 1;
		PrimitiveDenseStore ai = PrimitiveDenseStore.FACTORY.makeEye(n, n);
		for (int r = 0; r < n; r++)
			for (int c = 0; c < n; c++)
				if (r == c)
					ai.set(r, c, -1);

		for (int c = 0; c < n; c++)
			ai.set(n - 1, c, 1);

		return ai;
	}

	private MatrixStore<Double> BI()
	{
		int n = numFeatures() + 1;
		PrimitiveDenseStore bi = PrimitiveDenseStore.FACTORY.makeZero(n, 1);
		bi.set(n - 1, 0, 2);
		return bi;
	}

	private void finalizeLambdas(Result result)
	{
		int n = numFeatures();
		lambdas = new DenseMatrix64F(n, 1);
		for (int r = 0; r < n; r++)
			lambdas.set(r, 0, result.getSolution().doubleValue(r, 0));
	}

	private int numFeatures()
	{
		return featureMatrix[0].length;
	}

	private int numQueries()
	{
		return featureMatrix.length;
	}

	private int numInterpretations(int q)
	{
		return featureMatrix[q][0].numRows();
	}
	
	private int countTotalNumberOfInterpretations()
	{
		int count = 0;
		for(int q = 0; q < numQueries(); q++)
			count  += numInterpretations(q);
		
		return count;
	}

	public static void main(String[] args)
	{
		LeastSquareTrainerQP ls = new LeastSquareTrainerQP();

		SimpleMatrix[][] Mf = new SimpleMatrix[1][];
		Mf[0] = new SimpleMatrix[]
		{ 
				new SimpleMatrix(new double[][] // Feature 1	
					{
						{ 1, -1, -1 },
						{ 1, 2, -10 },
						{ 2, -3, 3 }
					}),
				
				new SimpleMatrix(new double[][] // Feature 2	
					{
						{ 2, -2, -4 },
						{ 3, 10, 3 },
						{ 2, -1, 3 }
					}),
					
				new SimpleMatrix(new double[][] // Feature 3	
					{
						{ 1, 2, -7 },
						{ 2, 1, 1 },
						{ 2, 2, 8 }
					}),
		};
		
		SimpleMatrix[] rStar = new SimpleMatrix[1];
		rStar[0] = new SimpleMatrix(new double[][]
			{
				{ 9 },
				{ 5 },
				{ 1 }
			});
		
		SimpleMatrix[] b = new SimpleMatrix[1];
		b[0] = new SimpleMatrix(new double[][]
			{
				{ 7 },
				{ 33 },
				{ 21 }
			});
		
		ls.train(Mf, rStar, b);
		
		System.out.println("Lambdas");
		System.out.println(ls.getLambdas().toString());
	}
}
