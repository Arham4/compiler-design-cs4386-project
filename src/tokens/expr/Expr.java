package tokens.expr;

import tokens.NonTerminalToken;
import tokens.id.Name;
import tokens.lexeme.Lexeme;
import tokens.lexeme.Type;
import tokens.lexeme.Types;
import tokens.methods.args.Args;
import type_checking.TypeCheckException;
import type_checking.TypeCheckable;

import java.util.Map;

import static type_checking.TypeCheckException.undeclaredError;
import static utils.SymbolTableHelper.getClosestScopeType;
import static utils.SymbolTableHelper.isScopeTooHigh;

public interface Expr extends NonTerminalToken, TypeCheckable<Type> {
    static Expr simple(Name name) {
        return new Expr() {
            @Override
            public String asString(int tabs) {
                return name.asString(tabs);
            }

            @Override
            public Type typeCheck(int scope, Map<String, Map<Integer, Type>> variableSymbolTable, Map<String, Type> methodSymbolTable) throws TypeCheckException {
                if (!variableSymbolTable.containsKey(name.getId()) || isScopeTooHigh(scope, variableSymbolTable.get(name.getId()))) {
                    throw undeclaredError(name.getId());
                }
                return getClosestScopeType(scope, variableSymbolTable.get(name.getId()));
            }
        };
    }

    static Expr simpleInvocation(String id) {
        return new Expr() {
            @Override
            public String asString(int tabs) {
                return id + "()";
            }

            @Override
            public Type typeCheck(int scope, Map<String, Map<Integer, Type>> variableSymbolTable, Map<String, Type> methodSymbolTable) throws TypeCheckException {
                if (!methodSymbolTable.containsKey(id)) {
                    throw undeclaredError(id);
                }
                return methodSymbolTable.get(id);
            }
        };
    }

    static Expr invocation(String id, Args args) {
        return new Expr() {
            @Override
            public String asString(int tabs) {
                return id + "(" + args.asString(tabs) + ")";
            }

            @Override
            public Type typeCheck(int scope, Map<String, Map<Integer, Type>> variableSymbolTable, Map<String, Type> methodSymbolTable) throws TypeCheckException {
                if (!methodSymbolTable.containsKey(id)) {
                    throw undeclaredError(id);
                }
                // todo check args
                return methodSymbolTable.get(id);
            }
        };
    }

    static Expr intlit(int integer) {
        return new Expr() {
            @Override
            public String asString(int tabs) {
                return integer + "";
            }

            @Override
            public Type typeCheck(int scope, Map<String, Map<Integer, Type>> variableSymbolTable, Map<String, Type> methodSymbolTable) throws TypeCheckException {
                return Types.INTLIT;
            }
        };
    }

    static Expr charlit(String character) {
        return new Expr() {
            @Override
            public String asString(int tabs) {
                return character;
            }

            @Override
            public Type typeCheck(int scope, Map<String, Map<Integer, Type>> variableSymbolTable, Map<String, Type> methodSymbolTable) throws TypeCheckException {
                return Types.CHARLIT;
            }
        };
    }

    static Expr strlit(String string) {
        return new Expr() {
            @Override
            public String asString(int tabs) {
                return string;
            }

            @Override
            public Type typeCheck(int scope, Map<String, Map<Integer, Type>> variableSymbolTable, Map<String, Type> methodSymbolTable) throws TypeCheckException {
                return Types.STR;
            }
        };
    }

    static Expr floatlit(double floatingPoint) {
        return new Expr() {
            @Override
            public String asString(int tabs) {
                return floatingPoint + "";
            }

            @Override
            public Type typeCheck(int scope, Map<String, Map<Integer, Type>> variableSymbolTable, Map<String, Type> methodSymbolTable) throws TypeCheckException {
                return Types.FLOATLIT;
            }
        };
    }

    static Expr boollit(boolean value) {
        return new Expr() {
            @Override
            public String asString(int tabs) {
                return value + "";
            }

            @Override
            public Type typeCheck(int scope, Map<String, Map<Integer, Type>> variableSymbolTable, Map<String, Type> methodSymbolTable) throws TypeCheckException {
                return Types.BOOLLIT;
            }
        };
    }

    static Expr parenthesized(Expr expr) {
        return new Expr() {
            @Override
            public String asString(int tabs) {
                return "(" + expr.asString(tabs) + ")";
            }

            @Override
            public Type typeCheck(int scope, Map<String, Map<Integer, Type>> variableSymbolTable, Map<String, Type> methodSymbolTable) throws TypeCheckException {
                return expr.typeCheck(scope, variableSymbolTable, methodSymbolTable);
            }
        };
    }

    static Expr not(Expr expr) {
        return new Expr() {
            @Override
            public String asString(int tabs) {
                return "~" + expr.asString(tabs);
            }

            @Override
            public Type typeCheck(int scope, Map<String, Map<Integer, Type>> variableSymbolTable, Map<String, Type> methodSymbolTable) throws TypeCheckException {
                Type exprType = expr.typeCheck(scope, variableSymbolTable, methodSymbolTable);
                if (exprType != Types.BOOLLIT && exprType != Types.INTLIT) {
                    throw TypeCheckException.withFault("Error: Not operation only applicable on bool (or coerced to bool)");
                }
                return Types.BOOLLIT;
            }
        };
    }

    static Expr minus(Expr expr) {
        return new Expr() {
            @Override
            public String asString(int tabs) {
                return "-" + expr.asString(tabs);
            }

            @Override
            public Type typeCheck(int scope, Map<String, Map<Integer, Type>> variableSymbolTable, Map<String, Type> methodSymbolTable) throws TypeCheckException {
                Type exprType = expr.typeCheck(scope, variableSymbolTable, methodSymbolTable);
                if (exprType != Types.INTLIT && exprType != Types.FLOATLIT) {
                    throw TypeCheckException.withFault("Error: Negative operation only applicable on int or float");
                }
                return exprType;
            }
        };
    }

    static Expr plus(Expr expr) {
        return new Expr() {
            @Override
            public String asString(int tabs) {
                return "+" + expr.asString(tabs);
            }

            @Override
            public Type typeCheck(int scope, Map<String, Map<Integer, Type>> variableSymbolTable, Map<String, Type> methodSymbolTable) throws TypeCheckException {
                return null;
            }
        };
    }

    static Expr casting(Type type, Expr expr) {
        return new Expr() {
            @Override
            public String asString(int tabs) {
                return "(" + type.asString(tabs) + ") " + expr.asString(tabs);
            }

            @Override
            public Type typeCheck(int scope, Map<String, Map<Integer, Type>> variableSymbolTable, Map<String, Type> methodSymbolTable) throws TypeCheckException {
                return null;
            }
        };
    }

    static Expr binaryOp(Expr expr1, Lexeme binaryOp, Expr expr2) {
        return new Expr() {
            @Override
            public String asString(int tabs) {
                return "(" + expr1.asString(tabs) + " " + binaryOp.asString(tabs) + " " + expr2.asString(tabs) + ")";
            }

            @Override
            public Type typeCheck(int scope, Map<String, Map<Integer, Type>> variableSymbolTable, Map<String, Type> methodSymbolTable) throws TypeCheckException {
                return null;
            }
        };
    }

    static Expr ternary(Expr expr, Expr exprTrue, Expr exprFalse) {
        return new Expr() {
            @Override
            public String asString(int tabs) {
                return "(" + expr.asString(tabs) + ") ? " + exprTrue.asString(tabs) + " : " + exprFalse.asString(tabs) + ")";
            }

            @Override
            public Type typeCheck(int scope, Map<String, Map<Integer, Type>> variableSymbolTable, Map<String, Type> methodSymbolTable) throws TypeCheckException {
                return null;
            }
        };
    }
}
