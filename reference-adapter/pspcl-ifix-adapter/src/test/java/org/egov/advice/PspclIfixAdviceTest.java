package org.egov.advice;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PspclIfixAdviceTest {

    @Test
    void testLogExecutionTime3() throws Throwable {
        PspclIfixAdvice pspclIfixAdvice = new PspclIfixAdvice();
        Signature signature = mock(Signature.class);
        when(signature.getDeclaringTypeName()).thenReturn("Declaring Type Name");
        when(signature.getName()).thenReturn("Name");
        ProceedingJoinPoint proceedingJoinPoint = mock(ProceedingJoinPoint.class);
        when(proceedingJoinPoint.proceed()).thenReturn("Proceed");
        when(proceedingJoinPoint.getSignature()).thenReturn(signature);
        assertEquals("Proceed", pspclIfixAdvice.logExecutionTime(proceedingJoinPoint));
        verify(proceedingJoinPoint).proceed();
        verify(proceedingJoinPoint, atLeast(1)).getSignature();
        verify(signature).getDeclaringTypeName();
        verify(signature).getName();
    }
}

