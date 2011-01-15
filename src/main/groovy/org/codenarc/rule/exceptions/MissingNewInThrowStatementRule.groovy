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
package org.codenarc.rule.exceptions

import org.codenarc.rule.AbstractAstVisitor
import org.codenarc.rule.AbstractAstVisitorRule
import org.codehaus.groovy.ast.stmt.ThrowStatement
import org.codehaus.groovy.ast.expr.MethodCallExpression

/**
 * A common Groovy mistake when throwing exceptions is to forget the new keyword. For instance, "throw RuntimeException()"
 * instead of "throw new RuntimeException()". If the error path is not unit tested then the production system will throw
 * a Method Missing exception and hide the root cause. This rule finds constructs like "throw RuntimeException()" that
 * look like a new keyword was meant to be used but forgotten.
 *
 * @author Hamlet D'Arcy
 * @version $Revision: 24 $ - $Date: 2009-01-31 13:47:09 +0100 (Sat, 31 Jan 2009) $
 */
class MissingNewInThrowStatementRule extends AbstractAstVisitorRule {
    String name = 'MissingNewInThrowStatement'
    int priority = 2
    Class astVisitorClass = MissingNewInThrowStatementAstVisitor
}

class MissingNewInThrowStatementAstVisitor extends AbstractAstVisitor {
    @Override
    void visitThrowStatement(ThrowStatement statement) {

        if (statement.expression instanceof MethodCallExpression) {
            String name = statement?.expression?.method?.properties['value']
            if (isFirstLetterUpperCase(name)) {
                if (name.endsWith('Exception') || name.endsWith('Failure')|| name.endsWith('Fault')) {
                    addViolation (statement, "The throw statement appears to be throwing the class literal $name instead of a new instance")
                }
            }
        }
        super.visitThrowStatement(statement)
    }

    private static boolean isFirstLetterUpperCase(String name) {
        return name && (name[0] as Character).isUpperCase()
    }
}
