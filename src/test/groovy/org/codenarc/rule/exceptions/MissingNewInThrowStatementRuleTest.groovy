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
 * Tests for MissingNewInThrowStatementRule
 *
 * @author Hamlet D'Arcy
 * @version $Revision: 329 $ - $Date: 2010-04-29 04:20:25 +0200 (Thu, 29 Apr 2010) $
 */
class MissingNewInThrowStatementRuleTest extends AbstractRuleTestCase {

    void testRuleProperties() {
        assert rule.priority == 2
        assert rule.name == "MissingNewInThrowStatement"
    }

    void testSuccessScenario() {
        final SOURCE = '''
            throw runtimeFailure()      
            throw RuntimeObject()
            throw new RuntimeException()
        '''
        assertNoViolations(SOURCE)
    }

    void testException() {
        final SOURCE = '''
            throw RuntimeException()    // ends in Exceptions, first letter Capitalized
        '''
        assertSingleViolation(SOURCE, 2, 'throw RuntimeException()', 'The throw statement appears to be throwing the class literal RuntimeException instead of a new instance')
    }

    void testFailure() {
        final SOURCE = '''
            throw RuntimeFailure()      // ends in Failure, first letter Capitalized
        '''
        assertSingleViolation(SOURCE, 2, 'throw RuntimeFailure()', 'The throw statement appears to be throwing the class literal RuntimeFailure instead of a new instance')
    }

    void testFault() {
        final SOURCE = '''
            throw RuntimeFault(foo)     // ends in Fault, first letter Capitalized
        '''
        assertSingleViolation(SOURCE, 2, 'throw RuntimeFault(foo)', 'The throw statement appears to be throwing the class literal RuntimeFault instead of a new instance')
    }

    protected Rule createRule() {
        new MissingNewInThrowStatementRule()
    }
}