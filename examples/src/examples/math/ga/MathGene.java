/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.math.ga;

import java.io.*;
import org.jgap.*;
import examples.math.*;
import examples.math.cmd.*;

/**
 * A gene for mathematical expressions.
 *
 * @author Michael Grove
 * @since 3.4.2
 */
public class MathGene
    extends BaseGene implements Gene, Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private Operand mOperand;

  public MathGene(Configuration theConfiguration)
      throws InvalidConfigurationException {
    super(theConfiguration);
    mOperand = new ValueOperand(0);
  }

  protected Object getInternalValue() {
    return mOperand;
  }

  protected Gene newGeneInternal() {
    try {
      return new MathGene(getConfiguration());
    } catch (InvalidConfigurationException e) {
      throw new IllegalStateException(e);
    }
  }

  public void setAllele(Object o) {
    if (o instanceof Operand) {
      mOperand = (Operand) o;
    }
  }

  public String getPersistentRepresentation()
      throws UnsupportedOperationException {
    throw new RuntimeException("NYI");
  }

  public void setValueFromPersistentRepresentation(String s)
      throws UnsupportedOperationException, UnsupportedRepresentationException {
    throw new RuntimeException("NYI");
  }

  public void setToRandomValue(RandomGenerator theRandomGenerator) {
    setAllele(generateRandomOperand(theRandomGenerator));
  }

  private Operand generateRandomOperand(RandomGenerator theRand) {
    if (theRand.nextBoolean()) {
      return generateRandomValueOperand(theRand);
    }
    else {
      return generateRandomMathOperator(theRand);
    }
  }

  private ValueOperand generateRandomValueOperand(RandomGenerator theRand) {
    // TODO: generate negative value?
    // TODO: should there be bounds applied?
    return new ValueOperand(theRand.nextDouble() *
                            theRand.nextInt(Integer.MAX_VALUE));
  }

  private MathOperator generateRandomMathOperator(RandomGenerator theRand) {
    MathOperator aOp = null;
    int aType = theRand.nextInt(4);
    if (aType == 0) {
      aOp = new AddOperator();
    }
    else if (aType == 1) {
      aOp = new MinusOperator();
    }
    else if (aType == 2) {
      aOp = new MultiplyOperator();
    }
    else if (aType == 3) {
      aOp = new DivideOperator();
    }
    aOp.setLeftOperand(generateRandomValueOperand(theRand));
    aOp.setRightOperand(generateRandomValueOperand(theRand));
    return aOp;
  }

  public void applyMutation(int i, double v) {
    double aRate = Math.abs(v);
    setAllele(mutateOperand(mOperand, aRate));
  }

  private Operand mutateOperand(Operand theOp, double theRate) {
    Operand aReturn = null;
    if (theOp instanceof ValueOperand) {
      RandomGenerator aRand = getConfiguration().getRandomGenerator();
      if (aRand.nextDouble() < theRate) {
        // the bigger the rate, the more likely this switches from a set value to a
        // math operation...
        MathOperator aOp = generateRandomMathOperator(aRand);
        // we switch this operand to a math operation, but lets keep its value as one
        // of the operands of the operator
        aOp.setLeftOperand(theOp);
        aReturn = aOp;
      }
      else {
        // lets just mutate the value
        ValueOperand aOp = (ValueOperand) theOp;
        aOp.setValue(aOp.value() * (1 + (theRate - .5)));
        aReturn = aOp;
      }
    }
    else if (theOp instanceof MathOperator) {
      MathOperator aOp = (MathOperator) theOp;
      RandomGenerator aRand = getConfiguration().getRandomGenerator();
      if (aRand.nextDouble() < theRate) {
        // if there's a value operand in the math operation, pull it out,
        // and we'll use that.  otherwise we'll generate a random one.
        ValueOperand aVal = new GetValueOperandVisitor().find(aOp);
        if (aVal == null) {
          aVal = generateRandomValueOperand(aRand);
        }
        aReturn = aVal;
      }
      else {
        int aType = aRand.nextInt(3);
        if (aType == 0) {
          // switch the math operation used
          MathOperator aNewOp = generateRandomMathOperator(aRand);
          aNewOp.setLeftOperand(aOp.getLeftOperand());
          aNewOp.setRightOperand(aOp.getRightOperand());
          aReturn = aNewOp;
        }
        else if (aType == 1) {
          aOp.setLeftOperand(generateRandomOperand(aRand));
          aReturn = aOp;
        }
        else if (aType == 2) {
          aOp.setRightOperand(generateRandomOperand(aRand));
          aReturn = aOp;
        }
      }
    }
    else {
      throw new IllegalStateException();
    }
    return aReturn;
  }

  @Override
  public int hashCode() {
    return toString().hashCode();
  }

  @Override
  public String toString() {
    return new ReplVisitor().render(mOperand) + " = " + mOperand.value();
  }

  @Override
  public boolean equals(Object theObj) {
    if (theObj instanceof MathGene) {
      MathGene aGene = (MathGene) theObj;
      return aGene.toString().equals(toString());
    }
    else {
      return false;
    }
  }

  public int compareTo(Object o) {
    if (o instanceof MathGene) {
      MathGene aGene = (MathGene) o;
      return new Double(mOperand.value()).compareTo(aGene.mOperand.value());
      //return new ReplVisitor().render(mOperand).compareTo(new ReplVisitor().render(aGene.mOperand));
    }
    else {
      throw new ClassCastException();
    }
  }

  private class GetValueOperandVisitor
      implements MathVisitor {
    private ValueOperand mOp;

    public ValueOperand find(MathOperator theOp) {
      theOp.accept(this);
      return mOp;
    }

    public void visit(ValueOperand theOp) {
      if (mOp == null) {
        mOp = theOp;
      }
    }

    private void mathOpVisit(MathOperator theOp) {
      if (mOp == null) {
        theOp.getLeftOperand().accept(this);
      }
      if (mOp == null) {
        theOp.getRightOperand().accept(this);
      }
    }

    public void visit(AddOperator theOp) {
      mathOpVisit(theOp);
    }

    public void visit(MinusOperator theOp) {
      mathOpVisit(theOp);
    }

    public void visit(DivideOperator theOp) {
      mathOpVisit(theOp);
    }

    public void visit(MultiplyOperator theOp) {
      mathOpVisit(theOp);
    }
  }
}
