
public class Position implements Comparable<Position> {

    private int X;
    private int Y;

    public Position(int X, int Y) {
        this.X = X;
        this.Y = Y;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    public double Range(Position p){ return Math.sqrt(RangeX(p)*RangeX(p)+RangeY(p)*RangeY(p)); }

    public int RangeX(Position p){ return getX()-p.getX(); }

    public int RangeY(Position p){
        return getY()-p.getY();
    }

    public Position Up(){
        return new Position(getX(), getY() - 1);
    }

    public Position Down(){
        return new Position(getX(), getY() + 1);
    }

    public Position Left(){
        return new Position(getX() - 1, getY());
    }

    public Position Right(){
        return new Position(getX() + 1, getY());
    }

    public Position NoOperation(){
        return new Position(getX() , getY());
    }

    @Override
    public int compareTo(Position o) { return (Y < o.Y) ? -1 : ((Y > o.Y) ? 1 : ((X < o.X) ? -1 : ((X > o.X) ? 1 : 0))); }


    //@Override
    // public int compareTo(Position o) { return (X < o.X) ? -1 : ((X > o.X) ? 1 : ((Y < o.Y) ? -1 : ((Y > o.Y) ? 1 : 0))); }
    @Override
    public boolean equals(Object o){
        return toString().equals(o.toString());
    }

    @Override
    public String toString(){
        return String.format("(%d, %d)", X,Y);
    }
}
