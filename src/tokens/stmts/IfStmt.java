package tokens.stmts;

import tokens.expr.Expr;
import utils.StringHelper;

public final class IfStmt implements Stmt {
    public static class Builder {
        private Expr expr;
        private Stmt stmt;
        private IfEnd ifEnd;

        public Builder expr(Expr expr) {
            this.expr = expr;
            return this;
        }

        public Builder stmt(Stmt stmt) {
            this.stmt = stmt;
            return this;
        }

        public Builder ifEnd(IfEnd ifEnd) {
            this.ifEnd = ifEnd;
            return this;
        }

        public IfStmt build() {
            return new IfStmt(expr, stmt, ifEnd);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private final Expr expr;
    private final Stmt stmt;
    private final IfEnd ifEnd;

    private IfStmt(Expr expr, Stmt stmt, IfEnd ifEnd) {
        this.expr = expr;
        this.stmt = stmt;
        this.ifEnd = ifEnd;
    }

    @Override
    public String asString(String prefix, int tabs) {
        if (stmt instanceof BodyStmt) {
            return prefix + "if (" + expr.asString(tabs) + ") " + stmt.asString("", tabs) + (ifEnd.isShow() ? "\n" + StringHelper.withTabs(tabs, ifEnd.asString(tabs)) : "");
        } else {
            return prefix + "if (" + expr.asString(tabs) + ")\n" + stmt.asString(StringHelper.tabs(tabs + 1), tabs + 1) + (ifEnd.isShow() ? "\n" + StringHelper.withTabs(tabs, ifEnd.asString(tabs)) : "");
        }
    }
}