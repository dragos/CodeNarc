/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codenarc.rule.exceptions

import org.codenarc.rule.AbstractRuleTestCase
import org.codenarc.rule.Rule

/**
 * Tests for ConfusingClassNamedExceptionRule
 *
 * @author Your Name Here
 * @version $Revision$ - $Date$
 */
class ConfusingClassNamedExceptionRuleTest extends AbstractRuleTestCase {

    void testRuleProperties() {
        assert rule.priority == 2
        assert rule.name == "ConfusingClassNamedException"
    }

    void testNoViolations() {
        final SOURCE = '''
        	class MyClass {}
        	class MyException extends Exception {}
        	class MyThrowableException extends Throwable {}
        	class MyRuntimeException extends RuntimeException{}
        	class MyIllegalStateException extends IllegalStateException{}
            class MyExceptionSubclass extends MyException {}
            class MyClass {
        	    class MyNestedException extends Exception {}
            }
        '''
        assertNoViolations(SOURCE)
    }

    void testExceptionWithParent() {
        final SOURCE = '''
            class MyException extends PrintWriter {}
        '''
        assertSingleViolation(SOURCE, 2, 'class MyException extends PrintWriter {}')
    }

    void testExceptionWithoutParent() {
        final SOURCE = '''
            class MyException {}
        '''
        assertSingleViolation(SOURCE, 2, 'class MyException {}')
    }
      // todo: uncomment when groovy 1.7 is supported
//    void testExceptionInNestedClass() {
//        final SOURCE = '''
//        class MyClass {
//            class MyNestedException {}
//        }
//        '''
//        assertSingleViolation(SOURCE, 3, 'class MyNestedException {}')
//    }
    protected Rule createRule() {
        return new ConfusingClassNamedExceptionRule()
    }
}