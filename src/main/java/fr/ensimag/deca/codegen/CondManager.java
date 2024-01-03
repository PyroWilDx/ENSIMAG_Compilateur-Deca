package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Label;

import java.util.LinkedList;

public class CondManager {

    public enum Operation {
        NONE,
        AND,
        OR
    }

    private int idCpt;
    private int currNotCpt;
    private LinkedList<Label> condTrueLabelStack;
    private LinkedList<Label> condFalseLabelStack;
    private LinkedList<Operation> operationStack;

    public CondManager() {
        this.idCpt = 0;
        this.currNotCpt = 0;
        this.condTrueLabelStack = new LinkedList<>();
        this.condFalseLabelStack = new LinkedList<>();
        this.operationStack = new LinkedList<>();
    }

    public int getAndIncrIdCpt() {
        return idCpt++;
    }

    public void incrNotCpt() {
        currNotCpt++;
    }

    public void decrNotCpt() {
        currNotCpt--;
    }

    public boolean isInNot() {
        return currNotCpt % 2 != 0;
    }

    public boolean isInCond() {
        return !condTrueLabelStack.isEmpty() || !condFalseLabelStack.isEmpty();
    }

    public void popCondLabels() {
        condTrueLabelStack.removeFirst();
        condFalseLabelStack.removeFirst();
    }

    public void addCondLabels(Label condTrueLabel, Label condFalseLabel) {
        condTrueLabelStack.addFirst(condTrueLabel);
        condFalseLabelStack.addFirst(condFalseLabel);
    }

    public Label getCurrCondTrueLabel() {
        if (isInNot()) return condFalseLabelStack.peekFirst();
        return condTrueLabelStack.peekFirst();
    }

    public Label getCurrCondFalseLabel() {
        if (isInNot()) return condTrueLabelStack.peekFirst();
        return condFalseLabelStack.peekFirst();
    }

    public boolean isInAnd() {
        return operationStack.peekFirst() == Operation.AND;
    }

    public boolean isInOr() {
        return operationStack.peekFirst() == Operation.OR;
    }

    public void popOperation() {
        operationStack.removeFirst();
    }

    public void addAndOperation() {
        operationStack.addFirst(Operation.AND);
    }

    public void addOrOperation() {
        operationStack.addFirst(Operation.OR);
    }

}
