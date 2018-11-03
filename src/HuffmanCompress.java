import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
	//临时存储字符频度的数组
	private CharacterFreq[] tmp_nodes = new CharacterFreq[256];
	private int file_len = 0;
	private int char_kinds = 0;
	
	public HuffmanCompress(){
    	//初始化字符频度数组
        for(int i = 0; i < 256; ++i){
           tmp_nodes[i] = new CharacterFreq(i);
       }
       queue = new PriorityQueue<HufTree>(12, new Compare());
	}
	
	public void init(){
		file_len = 0;
		char_kinds = 0;
		queue.clear();
		huf_tree = null;
	}
	
	
    //读取文件
    public int read_file(FileInputStream fis) throws IOException {
		try {
			int char_temp;
			 //统计字符频度，计算文件长度
			while ((char_temp = fis.read()) != -1) { // 一次读入一行，当读入空时停止读取
				//char_temp = tempString;
				++tmp_nodes[char_temp].weight;
                ++file_len;
				//System.out.println(char_temp);
			}
			Arrays.sort(tmp_nodes);
			
		} finally {
			if (fis != null) {

			}
		}
		return file_len;
	}
    
    //读取文件
    public void compress_write_file_kind1(ObjectOutputStream oos) throws FileNotFoundException, IOException {
		//File file = new File(fileName);
		//ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
		try {
			 //写入字节种类
			oos.writeInt(char_kinds);
			oos.writeByte(tmp_nodes[0].uch);
			oos.writeInt(tmp_nodes[0].weight);
			oos.close();
		} catch (IOException e) { // 异常处理
			e.printStackTrace();
		} finally {
			if (oos != null) {
			}
		}
	}
    
    public void compress_write_file_kindN(FileInputStream fis, ObjectOutputStream oos) throws FileNotFoundException, IOException {
		//File outputFile = new File(outputfileName);
		//File inputFile = new File(inputfileName);
		//ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outputFile));
		//映射字节及其对应的哈夫曼编码
		HashMap<Byte,String> map = new HashMap<Byte,String>();
		//FileInputStream fis = new FileInputStream(inputFile);
		String code_buf = null;
		int char_temp = 0;
		try {
			oos.writeInt(char_kinds);
            for(int i = 0; i < char_kinds; ++i){
                oos.writeByte(huf_tree[i].uch);
                oos.writeInt(huf_tree[i].weight);
                map.put(huf_tree[i].uch, huf_tree[i].code);
            }
            
            oos.writeInt(file_len);
            
            code_buf = "";
            //将读出的字节对应的哈夫曼编码转化为二进制存入文件
            
            while((char_temp = fis.read()) != -1){

                code_buf += map.get((byte)char_temp);

                while(code_buf.length() >= 8){
                    char_temp = 0;
                    for(int i = 0; i < 8; ++i){
                        char_temp <<= 1;
                        if(code_buf.charAt(i) == '1')
                            char_temp |= 1;
                    }
                    
                    code_buf = code_buf.substring(8);
                    oos.writeByte((byte)char_temp);
                }
            }
            //最后编码长度不够8位的时候，用0补齐
            if(code_buf.length() > 0){
                char_temp = 0;
                for(int i = 0; i < code_buf.length(); ++i){
                    char_temp <<= 1;
                    if(code_buf.charAt(i) == '1')
                        char_temp |= 1;
                }
                char_temp <<= (8-code_buf.length());
                oos.writeByte((byte)char_temp);
            }
            fis.close();
			oos.close();
		} catch (IOException e) { // 异常处理
			e.printStackTrace();
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e1) {
				}
			}
		}
	}
    
    public int extract_write_file_kind1(ObjectInputStream ois2, FileOutputStream fos2) throws IOException{
        byte code_temp = 0;
        int filelen = 0;
    	try {
            code_temp = ois2.readByte();
            filelen = ois2.readInt();
            while((filelen--) != 0){
                fos2.write(code_temp);
            }
            //fos2.close();
            //ois2.close();
		} catch (IOException e) { // 异常处理
			e.printStackTrace();
		} finally {
			if (fos2 != null || ois2 != null) {
				try {
					fos2.close();
					ois2.close();
				} catch (IOException e1) {
				}
			}
		}
    	return filelen;
    }
    
    public void extract_write_file_kindN(String inputfileName, String outputfileName) throws FileNotFoundException, IOException {
		File outputFile = new File(outputfileName);
		File inputFile = new File(inputfileName);
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outputFile));
		//映射字节及其对应的哈夫曼编码
		HashMap<Byte,String> map = new HashMap<Byte,String>();
		FileInputStream fis = new FileInputStream(inputFile);
		String code_buf = null;
		int char_temp = 0;
		try {
			oos.writeInt(char_kinds);
            for(int i = 0; i < char_kinds; ++i){
                oos.writeByte(huf_tree[i].uch);
                oos.writeInt(huf_tree[i].weight);
                map.put(huf_tree[i].uch, huf_tree[i].code);
            }
            oos.writeInt(file_len);
            
            code_buf = "";
            //将读出的字节对应的哈夫曼编码转化为二进制存入文件
            
            while((char_temp = fis.read()) != -1){

                code_buf += map.get((byte)char_temp);

                while(code_buf.length() >= 8){
                    char_temp = 0;
                    for(int i = 0; i < 8; ++i){
                        char_temp <<= 1;
                        if(code_buf.charAt(i) == '1')
                            char_temp |= 1;
                    }
                    
                    code_buf = code_buf.substring(8);
                    oos.writeByte((byte)char_temp);
                }
            }
            //最后编码长度不够8位的时候，用0补齐
            if(code_buf.length() > 0){
                char_temp = 0;
                for(int i = 0; i < code_buf.length(); ++i){
                    char_temp <<= 1;
                    if(code_buf.charAt(i) == '1')
                        char_temp |= 1;
                }
                char_temp <<= (8-code_buf.length());
                oos.writeByte((byte)char_temp);
            }
            fis.close();
			oos.close();
		} catch (IOException e) { // 异常处理
			e.printStackTrace();
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e1) {
				}
			}
		}
	}
    
    //排序后就会将频度为0的字节放在数组最后，从而去除频度为0的字节
    //同时计算出字节的种类
    private int cal_char_kinds(){
    	int i;
    	for(i = 0; i < 256; i++){
             if(tmp_nodes[i].weight == 0)
                 break;
         }
    	 return i;
    }
    
    private void init_queue(int nodeNum, boolean extract, ObjectInputStream ois2) throws IOException{  	
    	int i= 0;
        huf_tree = new HufTree[nodeNum];
    	if(extract == false){
	        for(i = 0; i < char_kinds; i++){
	            huf_tree[i] = new HufTree();
	            huf_tree[i].uch = tmp_nodes[i].uch;
	            huf_tree[i].weight = tmp_nodes[i].weight;
	            huf_tree[i].parent = 0;
	            huf_tree[i].index = i;
	            queue.add(huf_tree[i]);
	        }
	        tmp_nodes = null;
    	}else{
    		for(i = 0; i < char_kinds; i++){
	            huf_tree[i] = new HufTree();
	            huf_tree[i].uch = ois2.readByte();
	            huf_tree[i].weight = ois2.readInt();
	            huf_tree[i].parent = 0;
	            huf_tree[i].index = i;
	            queue.add(huf_tree[i]);
	        }
    		//ois2.close();
    	}
        //System.out.println(i);
        for(; i < nodeNum; ++i){
            huf_tree[i] = new HufTree();
            huf_tree[i].parent = 0;
        }
        
    }
    
    //压缩函数
    public void compress(String inputName, String outputName){   
    	File inputFile = new File(inputName);
    	File outputFile = new File(outputName);
    	FileInputStream fis = null;
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fis = new FileInputStream(inputFile);
            fos = new FileOutputStream(outputFile);
            oos = new ObjectOutputStream(fos);
            file_len = read_file(fis);
            fis.close();
            char_kinds = cal_char_kinds();
            
            //只有一种字节的情况
            if(char_kinds == 1){
            	compress_write_file_kind1(oos);
            //字节多于一种的情况
            }else{
            	int node_num = 2*char_kinds-1;//计算哈夫曼树所有节点个数
                init_queue(node_num, false ,null);               
                //创建哈夫曼树
                createTree(huf_tree, char_kinds, node_num, queue);
                //生成哈夫曼编码
                hufCode(huf_tree, char_kinds);
                fis = new FileInputStream(inputFile);
                compress_write_file_kindN(fis, oos);               
                fis.close();
            }
            oos.close();
        } catch (Exception e) { 
            e.printStackTrace();
        }

    }

    //解压函数
    public void extract(String inputName, String outputName){//File inputFile, File outputFile){
    	File inputFile = new File(inputName);
    	File outputFile = new File(outputName);
    	//Compare cmp = new Compare();
        //queue = new PriorityQueue<HufTree>(12,new Compare());

        int i;
        //int file_len = 0;
        int writen_len = 0;
        FileInputStream fis = null;
        FileOutputStream fos = null;
        ObjectInputStream ois = null;

        //int char_kinds = 0;
        int node_num;
        //HufTree[] huf_tree = null;
        byte code_temp;
        int root;
        try{
            fis = new FileInputStream(inputFile);
            ois = new ObjectInputStream(fis);
            fos = new FileOutputStream(outputFile);

            char_kinds = ois.readInt();

            //字节只有一种的情况
            if(char_kinds == 1){
                /*code_temp = ois.readByte();
                file_len = ois.readInt();
                while((file_len--) != 0){
                    fos.write(code_temp);
                }*/
            	file_len = extract_write_file_kind1(ois, fos);
            //字节多于一种的情况
            }else{
                node_num = 2 * char_kinds - 1; //计算哈夫曼树所有节点个数
                /*huf_tree = new HufTree[node_num];
                for(i = 0; i < char_kinds; ++i){
                    huf_tree[i] = new HufTree();
                    huf_tree[i].uch = ois.readByte();
                    huf_tree[i].weight = ois.readInt();
                    huf_tree[i].parent = 0;

                    huf_tree[i].index = i;
                    queue.add(huf_tree[i]);
                }
                for(;i < node_num; ++i){
                    huf_tree[i] = new HufTree();
                    huf_tree[i].parent = 0;
                }*/
                init_queue(node_num, true, ois) ;
                createTree(huf_tree, char_kinds, node_num,queue);

                file_len = ois.readInt();
                root = node_num-1;
                while(true){
                    code_temp = ois.readByte();
                    for(i = 0; i < 8; ++i){
                        if((code_temp & 128) == 128){
                            root = huf_tree[root].rchild;
                        }else{
                            root = huf_tree[root].lchild;
                        }

                        if(root < char_kinds){
                            fos.write(huf_tree[root].uch);
                            ++writen_len;
                            if(writen_len == file_len) break;
                            root = node_num - 1; //恢复为根节点的下标，匹配下一个字节
                        }
                        code_temp <<= 1;
                    }
                    //在压缩的时候如果最后一个哈夫曼编码位数不足八位则补0
                    //在解压的时候，补上的0之前的那些编码肯定是可以正常匹配到和他对应的字节
                    //所以一旦匹配完补的0之前的那些编码，写入解压文件的文件长度就和压缩之前的文件长度是相等的
                    //所以不需要计算补的0的个数
                    if(writen_len == file_len) break;
                }
            }
            fis.close();
            fos.close();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    //构建哈夫曼树
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

    //获取哈夫曼编码
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
