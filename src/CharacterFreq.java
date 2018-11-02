class CharacterFreq implements Comparable<CharacterFreq>{
    public byte uch;
    public int weight;

    @Override
    public int compareTo(CharacterFreq arg0) {
        if(this.weight < arg0.weight)
            return 1;
        else if(this.weight > arg0.weight)
            return -1;
        return 0;
    }
}