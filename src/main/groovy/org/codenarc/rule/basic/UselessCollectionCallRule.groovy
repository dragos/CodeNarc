/*
 * Copyright 2009 the original author or authors.
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

import org.codenarc.rule.AbstractAstVisitor
import org.codenarc.rule.AbstractAstVisitorRule
import org.codehaus.groovy.ast.expr.TupleExpression
import org.codehaus.groovy.ast.expr.MethodCallExpression

/**
 * Useless call to collections. This call doesn't make sense. For any collection c, calling c.containsAll(c) should
 * always be true, and c.retainAll(c) should have no effect.
 *
 * @author Hamlet D'Arcy
 * @version $Revision$ - $Date$
 */
class UselessCollectionCallRule extends AbstractAstVisitorRule {
    String name = 'UselessCollectionCall'
    int priority = 2
    Class astVisitorClass = UselessCollectionCallAstVisitor
}

class UselessCollectionCallAstVisitor extends AbstractAstVisitor {

    private static final List USELESS_METHOD_NAMES = ['retainAll', 'containsAll']

    def void visitMethodCallExpression(MethodCallExpression call) {

        if (USELESS_METHOD_NAMES.contains(call.method.text)) {
            String variableName = call.objectExpression.text
            if (call.arguments instanceof TupleExpression && call.arguments.expressions.size() == 1) {
                def argName = call.arguments.expressions[0].text
                if (argName == variableName) {
                    addViolation call
                }
            }
        }

        super.visitMethodCallExpression(call)
    }

}
