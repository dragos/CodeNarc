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
package org.codenarc.rule.basic

import org.codenarc.rule.AbstractRuleTestCase
import org.codenarc.rule.Rule

/**
 * Tests for UselessOverridingMethodRule
 *
 * @author 'Sven Lange'
 * @version $Revision$ - $Date$
 */
class UselessOverridingMethodRuleTest extends AbstractRuleTestCase {

    void testRuleProperties() {
        assert rule.priority == 2
        assert rule.name == "UselessOverridingMethod"
    }

    void testSuccessScenario() {
        final SOURCE = '''
        	class MyLabel extends javax.swing.JLabel {

                void setVisible(boolean value, int value2) {
                    super.setVisible(value)
                }

                void setVisible(String value) {
                    proxy.setVisible(value)
                }

                void setVisible(boolean value, String value2) {
                    super.setVisible(value2, value)
                }

                void someMethod(value) {
                    super.someMethod(value())
                }
            }
        '''
        assertNoViolations(SOURCE)
    }

    void testOneParameterViolation() {
        final SOURCE = '''
            class MyLabel extends javax.swing.JLabel {

                void setVisible(boolean value) {
                    super.setVisible(value)
                }
            }
        '''
        assertSingleViolation(SOURCE, 4, 'setVisible(boolean value)')
    }

    void testThreeParameterViolation() {
        final SOURCE = '''
            class MyLabel extends javax.swing.JLabel {

                void setVisible(boolean value1, boolean value2, boolean value3) {
                    super.setVisible(value1, value2, value3)
                }
            }
        '''
        assertSingleViolation(SOURCE, 4, 'void setVisible(boolean value1, boolean value2, boolean value3)')
    }

    protected Rule createRule() {
        return new UselessOverridingMethodRule()
    }
}