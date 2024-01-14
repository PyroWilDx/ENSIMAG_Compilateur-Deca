package fr.ensimag.deca.context;

import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;

/**
 * Test for the Plus node using mockito, using @Mock and @Before annotations.
 *
 * @author Ensimag
 * @date 01/01/2024
 */
public class TestPlusAdvanced {
    @Mock
    AbstractExpr intexpr1 = new IntLiteral(1);
    @Mock
    AbstractExpr intexpr2 = new IntLiteral(2);
    @Mock
    AbstractExpr floatexpr1 = new FloatLiteral(3);
    @Mock
    AbstractExpr floatexpr2 = new FloatLiteral(4);
    DecacCompiler compiler;
    EnvironmentExp envExp;

    @BeforeEach
    public void setup() throws ContextualError {
        MockitoAnnotations.initMocks(this);
        compiler = new DecacCompiler(new CompilerOptions(), null);
        envExp = new EnvironmentExp(null);
        when(intexpr1.verifyExpr(compiler, envExp, null)).thenReturn(compiler.environmentType.INT);
        when(intexpr2.verifyExpr(compiler, envExp, null)).thenReturn(compiler.environmentType.INT);
        when(floatexpr1.verifyExpr(compiler, envExp, null)).thenReturn(compiler.environmentType.FLOAT);
        when(floatexpr2.verifyExpr(compiler, envExp, null)).thenReturn(compiler.environmentType.FLOAT);
    }

    @Test
    public void testIntInt() throws ContextualError {
        Plus t = new Plus(intexpr1, intexpr2);
        // check the result
        assertTrue(t.verifyExpr(compiler, envExp, null).isInt());
        // check that the mocks have been called properly.
        verify(intexpr1).verifyExpr(compiler, envExp, null);
        verify(intexpr2).verifyExpr(compiler, envExp, null);
    }

    @Test
    public void testIntFloat() throws ContextualError {
        Plus t = new Plus(intexpr1, floatexpr1);
        // check the result
        assertTrue(t.verifyExpr(compiler, envExp, null).isFloat());
        // ConvFloat should have been inserted on the right side
        assertInstanceOf(ConvFloat.class, t.getLeftOperand());    // On s'attend que le int soit converti en float avec ConvFloat
        assertFalse(t.getRightOperand() instanceof ConvFloat);  // On s'attend que le float ne soit pas converti en
                                                                // ConvFloat car il est déjà en float
        // check that the mocks have been called properly.
        verify(intexpr1).verifyExpr(compiler, envExp, null);
        verify(floatexpr1).verifyExpr(compiler, envExp, null);
    }

    @Test
    public void testFloatInt() throws ContextualError {
        Plus t = new Plus(floatexpr1, intexpr1);
        // check the result
        assertTrue(t.verifyExpr(compiler, envExp, null).isFloat());
        // ConvFloat should have been inserted on the right side
        assertInstanceOf(ConvFloat.class, t.getRightOperand());
        assertFalse(t.getLeftOperand() instanceof ConvFloat);
        // check that the mocks have been called properly.
        verify(intexpr1).verifyExpr(compiler, envExp, null);
        verify(floatexpr1).verifyExpr(compiler, envExp, null);
    }
}