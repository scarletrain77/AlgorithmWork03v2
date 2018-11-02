import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.PriorityQueue;

public class HuffmanCompress {
	private PriorityQueue<HufTree> queue = null;
	private HufTree[] huf_tree = null;
	//��ʱ�洢�ַ�Ƶ�ȵ�����
	private CharacterFreq[] tmp_nodes = new CharacterFreq[256];
	private FileInputStream fis = null;
	private FileOutputStream fos = null;
	private int char_kinds = 0;
	
    public void character_freq(){
    	//��ʼ���ַ�Ƶ������
         for(int i = 0; i < 256; ++i){
            tmp_nodes[i] = new CharacterFreq();
            tmp_nodes[i].weight = 0;
            tmp_nodes[i].uch = (byte)i;
        }
    }
    //��ȡ�ļ�
    public int read_file(String fileName) {
    	int file_len = 0;
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			int tempString;
			int char_temp = 0;
			while ((tempString = reader.read()) != -1) { // һ�ζ���һ�У��������ʱֹͣ��ȡ
				char_temp = tempString;
				++tmp_nodes[char_temp].weight;
                ++file_len;
				//System.out.println(char_temp);
			}
			Arrays.sort(tmp_nodes);
			reader.close();
		} catch (IOException e) { // �쳣����
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return file_len;
	}
    
    private int cal_char_kinds(){
    	int i;
    	for(i = 0; i < 256; i++){
             if(tmp_nodes[i].weight == 0)
                 break;
         }
    	 return i;
    }
    
    private void init_queue(int node_num){
    	int i;
        huf_tree = new HufTree[node_num];
    	Compare cmp = new Compare();
    	queue = new PriorityQueue<HufTree>(12,cmp);
        for(i = 0; i < char_kinds; i++){
            huf_tree[i] = new HufTree();
            huf_tree[i].uch = tmp_nodes[i].uch;
            huf_tree[i].weight = tmp_nodes[i].weight;
            huf_tree[i].parent = 0;
            huf_tree[i].index = i;
            queue.add(huf_tree[i]);
        }
        tmp_nodes = null;
        System.out.println(i);
        for(; i < node_num; ++i){
            huf_tree[i] = new HufTree();
            huf_tree[i].parent = 0;
        }
    }
    
    //ѹ������
    public void compress(String inputName, String outputName){
    	File inputFile = new File(inputName);
    	File outputFile = new File(outputName);
    	
        //queue = new PriorityQueue<HufTree>(12,cmp);

        //ӳ���ֽڼ����Ӧ�Ĺ���������
        HashMap<Byte,String> map = new HashMap<Byte,String>();
        //�ļ��к��е��ַ���������
        int i;//,char_kinds = 0;
        int char_temp;//,file_len = 0;
        //FileInputStream fis = null;
        //FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        //���������ڵ����
        //int node_num;
        //HufTree[] huf_tree = null;
        String code_buf = null;
        //��ʼ������
        character_freq();
        //��ʼ������Դ����
        //CharacterFreq[] tmp_nodes = new CharacterFreq[256];
        //for(i = 0; i < 256; ++i){
        //    tmp_nodes[i] = new TmpNode();
        //    tmp_nodes[i].weight = 0;
        //    tmp_nodes[i].uch = (byte)i;
        //}
       
        try {
            //fis = new FileInputStream(inputFile);
            //fos = new FileOutputStream(outputFile);
            oos = new ObjectOutputStream(new FileOutputStream(outputFile));
            int file_len = read_file(inputName);
            //ͳ���ַ�Ƶ�ȣ������ļ�����
            //while((char_temp = fis.read()) != -1){
            //    ++tmp_nodes[char_temp].weight;
            //    ++file_len;
            //}
            //fis.close();
            //Arrays.sort(tmp_nodes);
            //�����ͻὫƵ��Ϊ0���ֽڷ���������󣬴Ӷ�ȥ��Ƶ��Ϊ0���ֽ�
            //ͬʱ������ֽڵ�����
            //int char_kinds = 0;
            //for(char_kinds = 0; char_kinds < 256; char_kinds++){
            //    if(tmp_nodes[char_kinds].weight == 0)
            //        break;
            //}
           // char_kinds = i;
           // System.out.println(char_kinds);
            char_kinds = cal_char_kinds();
            //ֻ��һ���ֽڵ����
            if(char_kinds == 1){
                oos.writeInt(char_kinds);
                oos.writeByte(tmp_nodes[0].uch);
                oos.writeInt(tmp_nodes[0].weight);
            //�ֽڶ���һ�ֵ����
            }else{
            	int node_num = 2*char_kinds-1;//��������������нڵ����
                init_queue(node_num);               
                //������������
                createTree(huf_tree, char_kinds, node_num,queue);
                //���ɹ���������
                hufCode(huf_tree, char_kinds);
                //д���ֽ�����
                oos.writeInt(char_kinds);
                for(i = 0; i < char_kinds; ++i){
                    oos.writeByte(huf_tree[i].uch);
                    oos.writeInt(huf_tree[i].weight);
                    map.put(huf_tree[i].uch, huf_tree[i].code);
                }
                oos.writeInt(file_len);
                fis = new FileInputStream(inputFile);
                code_buf = "";
                //���������ֽڶ�Ӧ�Ĺ���������ת��Ϊ�����ƴ����ļ�
                while((char_temp = fis.read()) != -1){

                    code_buf += map.get((byte)char_temp);

                    while(code_buf.length() >= 8){
                        char_temp = 0;
                        for(i = 0; i < 8; ++i){
                            char_temp <<= 1;
                            if(code_buf.charAt(i) == '1')
                                char_temp |= 1;
                        }
                        oos.writeByte((byte)char_temp);
                        code_buf = code_buf.substring(8);
                    }
                }
                //�����볤�Ȳ���8λ��ʱ����0����
                if(code_buf.length() > 0){
                    char_temp = 0;
                    for(i = 0; i < code_buf.length(); ++i){
                        char_temp <<= 1;
                        if(code_buf.charAt(i) == '1')
                            char_temp |= 1;
                    }
                    char_temp <<= (8-code_buf.length());
                    oos.writeByte((byte)char_temp);
                }
            }
            oos.close();
            fis.close();
        } catch (Exception e) { 
            e.printStackTrace();
        }

    }

    //��ѹ����
    public void extract(String inputName, String outputName){//File inputFile, File outputFile){
    	File inputFile = new File(inputName);
    	File outputFile = new File(outputName);
        Compare cmp = new Compare();
        queue = new PriorityQueue<HufTree>(12,cmp);

        int i;
        int file_len = 0;
        int writen_len = 0;
        FileInputStream fis2 = null;
        FileOutputStream fos2 = null;
        ObjectInputStream ois2 = null;

        int char_kinds = 0;
        int node_num;
        HufTree[] huf_tree = null;
        byte code_temp;
        int root;
        try{
            fis2 = new FileInputStream(inputFile);
            ois2 = new ObjectInputStream(fis2);
            fos2 = new FileOutputStream(outputFile);

            char_kinds = ois2.readInt();

            //�ֽ�ֻ��һ�ֵ����
            if(char_kinds == 1){
                code_temp = ois2.readByte();
                file_len = ois2.readInt();
                while((file_len--) != 0){
                    fos2.write(code_temp);
                }
            //�ֽڶ���һ�ֵ����
            }else{
                node_num = 2 * char_kinds - 1; //��������������нڵ����
                huf_tree = new HufTree[node_num];
                for(i = 0; i < char_kinds; ++i){
                    huf_tree[i] = new HufTree();
                    huf_tree[i].uch = ois2.readByte();
                    huf_tree[i].weight = ois2.readInt();
                    huf_tree[i].parent = 0;

                    huf_tree[i].index = i;
                    queue.add(huf_tree[i]);
                }
                for(;i < node_num; ++i){
                    huf_tree[i] = new HufTree();
                    huf_tree[i].parent = 0;
                }
                createTree(huf_tree, char_kinds, node_num,queue);

                file_len = ois2.readInt();
                root = node_num-1;
                while(true){
                    code_temp = ois2.readByte();
                    for(i = 0; i < 8; ++i){
                        if((code_temp & 128) == 128){
                            root = huf_tree[root].rchild;
                        }else{
                            root = huf_tree[root].lchild;
                        }

                        if(root < char_kinds){
                            fos2.write(huf_tree[root].uch);
                            ++writen_len;
                            if(writen_len == file_len) break;
                            root = node_num - 1; //�ָ�Ϊ���ڵ���±꣬ƥ����һ���ֽ�
                        }
                        code_temp <<= 1;
                    }
                    //��ѹ����ʱ��������һ������������λ�������λ��0
                    //�ڽ�ѹ��ʱ�򣬲��ϵ�0֮ǰ����Щ����϶��ǿ�������ƥ�䵽������Ӧ���ֽ�
                    //����һ��ƥ���겹��0֮ǰ����Щ���룬д���ѹ�ļ����ļ����Ⱦͺ�ѹ��֮ǰ���ļ���������ȵ�
                    //���Բ���Ҫ���㲹��0�ĸ���
                    if(writen_len == file_len) break;
                }
            }
            fis2.close();
            fos2.close();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    //������������
    public void createTree(HufTree[] huf_tree, int char_kinds, int node_num,PriorityQueue<HufTree> queue){
        int i;
        int[] arr = new int[2];
        for(i = char_kinds; i < node_num; ++i){
            arr[0] = queue.poll().index;          
            arr[1] = queue.poll().index;
            //System.out.println(arr[1]);
            huf_tree[arr[0]].parent = huf_tree[arr[1]].parent = i;
            huf_tree[i].lchild = arr[0];
            huf_tree[i].rchild = arr[1];
            huf_tree[i].weight = huf_tree[arr[0]].weight + huf_tree[arr[1]].weight;

            huf_tree[i].index = i;
            queue.add(huf_tree[i]);
        }
    }

    //��ȡ����������
    public void hufCode(HufTree[] huf_tree, int char_kinds){
        int i;
        int cur,next;

        for(i = 0; i < char_kinds; ++i){
            String code_tmp = "";
            for(cur = i,next = huf_tree[i].parent; next != 0; cur = next,next = huf_tree[next].parent){
                if(huf_tree[next].lchild == cur)
                    code_tmp += "0";
                else
                    code_tmp += "1";
            }
            huf_tree[i].code = (new StringBuilder(code_tmp)).reverse().toString();
        }
    }
}
