package tokens.stmts;

import tokens.methods.args.ReadList;

public final class ReadStmt implements Stmt {
    public static ReadStmt of(ReadList readList) {
        return new ReadStmt(readList);
    }

    private final ReadList readList;

    private ReadStmt(ReadList readList) {
        this.readList = readList;
    }

    @Override
    public String asString(String prefix, int tabs) {
        return prefix + "read(" + readList.asString(tabs) + ");";
    }
}