public class HufTree {
    public byte uch; //以8位为单元的字节
    public int weight;//该字节在文件中出现的次数
    public String code; //对应的哈夫曼编码
    //因为用数组存储的哈夫曼树
    //所以设置一个index记录当前节点在数组中的下标
    //用于在构建哈夫曼树的时候指明节点在数组中的位置
    public int index; 
    public int parent,lchild,rchild;

    /**调试的时候添加的**/
    public String toString(){
        return "uch:" + uch + ",weight:" + weight + ",code:" + code + ",parent:" + parent + ",lchild:" + lchild + ",rchild:" + rchild;
    }
}

