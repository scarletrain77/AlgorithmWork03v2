public class HufTree {
    public byte uch; //��8λΪ��Ԫ���ֽ�
    public int weight;//���ֽ����ļ��г��ֵĴ���
    public String code; //��Ӧ�Ĺ���������
    //��Ϊ������洢�Ĺ�������
    //��������һ��index��¼��ǰ�ڵ��������е��±�
    //�����ڹ�������������ʱ��ָ���ڵ��������е�λ��
    public int index; 
    public int parent,lchild,rchild;

    /**���Ե�ʱ����ӵ�**/
    public String toString(){
        return "uch:" + uch + ",weight:" + weight + ",code:" + code + ",parent:" + parent + ",lchild:" + lchild + ",rchild:" + rchild;
    }
}

