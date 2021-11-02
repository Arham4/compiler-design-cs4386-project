public final class ArrayArgDecl implements ArgDecl {
    public static class Builder {
        private Lexeme type;
        private String id;

        public Builder type(Lexeme type) {
            this.type = type;
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public ArrayArgDecl build() {
            return new ArrayArgDecl(type, id);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private final Lexeme type;
    private final String id;

    public ArrayArgDecl(Lexeme type, String id) {
        this.type = type;
        this.id = id;
    }

    @Override
    public String asString(int tabs) {
        return type.asString(tabs) + " " + id + "[]";
    }
}