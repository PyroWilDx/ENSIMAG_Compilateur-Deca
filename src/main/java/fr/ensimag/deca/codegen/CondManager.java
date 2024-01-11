package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Label;

import java.util.LinkedList;

public class CondManager {

    public enum Operation {
        AND,
        OR
    }

    private int idCpt;
    private boolean doingIfOrWhile;
    private boolean doingOpCmp;
    private final LinkedList<Label> condTrueLabelStack;
    private final LinkedList<Label> condFalseLabelStack;
    private final LinkedList<Operation> operationStack;

    public CondManager() {
        this.idCpt = 0;
        this.doingIfOrWhile = false;
        this.doingOpCmp = false;
        this.condTrueLabelStack = new LinkedList<>();
        this.condFalseLabelStack = new LinkedList<>();
        this.operationStack = new LinkedList<>();
    }

    public int getAndIncrIdCpt() {
        return idCpt++;
    }

    public void doIfOrWhile() {
        doingIfOrWhile = true;
    }

    public void exitIfOrWhile() {
        doingIfOrWhile = false;
    }

    public boolean isDoingIfOrWhile() {
        return doingIfOrWhile;
    }

    public void doOpCmp() {
        doingOpCmp = true;
    }

    public void exitOpCmp() {
        doingOpCmp = false;
    }

    public boolean isDoingOpCmp() {
        return doingOpCmp;
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
        return condTrueLabelStack.peekFirst();
    }

    public Label getCurrCondFalseLabel() {
        return condFalseLabelStack.peekFirst();
    }

    public Label getLastCondTrueLabel() {
        return condTrueLabelStack.peekLast();
    }

    public Label getLastCondFalseLabel() {
        return condFalseLabelStack.peekLast();
    }

    public boolean isInAnd() {
        return operationStack.peekFirst() == Operation.AND;
    }

    public boolean isInOr() {
        return operationStack.peekFirst() == Operation.OR;
    }

    public boolean areLastOpDiff() {
        if (operationStack.size() < 2) return true;
        return operationStack.get(0) != operationStack.get(1);
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