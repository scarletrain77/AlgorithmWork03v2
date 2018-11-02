class TmpNode implements Comparable<TmpNode>{
    public byte uch;
    public int weight;

    @Override
    public int compareTo(TmpNode arg0) {
        if(this.weight < arg0.weight)
            return 1;
        else if(this.weight > arg0.weight)
            return -1;
        return 0;
    }
}