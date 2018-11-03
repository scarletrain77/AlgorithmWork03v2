import java.io.*;
import java.util.Arrays;

//����
public class Test {

	public static CharacterFreq[] tmp_nodes = new CharacterFreq[256];
    public static void main(String[] args){
    	//����һ��1-6��������������ȡ�ļ�
    	int dataNumber = 2;// + (int)(Math.random()*6);
	    String fileName = "data/input_assign03_0" + Integer.toString(dataNumber);
    	//read_file("data/input_assign02_0" + Integer.toString(dataNumber) + ".txt");
	   
       
        String inputName = fileName + ".txt";
        String outputName = fileName + ".rar";
        //int file_len = read_file(inputName);
        HuffmanCompress sample = new HuffmanCompress();//tmp_nodes, file_len);
        sample.compress(inputName, outputName);
//        File inputFile = new File(file_name + ".txt");
//        File outputFile = new File(file_name + ".rar");
//        sample.compress(inputFile, outputFile);
        String inputName2 = fileName + "2.txt";
//      File inputFile = new File("gyh.rar");
//      File outputFile = new File("hyq.txt");
      sample.extract(outputName, inputName2);
    }
    

  //��ȡ�ļ�
    public static int read_file(String fileName) {
    	int file_len = 0;
		File file = new File(fileName);
		BufferedReader reader = null;
        for(int i = 0; i < 256; ++i){
            tmp_nodes[i] = new CharacterFreq(i);
            tmp_nodes[i].weight = 0;
            tmp_nodes[i].uch = (byte)i;
        }
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
    
}