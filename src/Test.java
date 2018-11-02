import java.io.*;

//测试
public class Test {

    public static void main(String[] args){
    	//产生一个1-6的随机数，随机读取文件
    	int dataNumber = 1;// + (int)(Math.random()*6);
	    String file_name = "data/input_assign03_0" + Integer.toString(dataNumber);
    	//read_file("data/input_assign02_0" + Integer.toString(dataNumber) + ".txt");
        HuffmanCompress sample = new HuffmanCompress();
        File inputFile = new File(file_name + ".txt");
        File outputFile = new File(file_name + ".rar");
        sample.compress(inputFile, outputFile);
//      File inputFile = new File("gyh.rar");
//      File outputFile = new File("hyq.txt");
//      sample.extract(inputFile, outputFile);
    }
    
    
    public static void read_file(String fileName) {
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) { // 一次读入一行，当读入空时停止读取

			}
			reader.close();
		} catch (IOException e) { // 异常处理
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}
}