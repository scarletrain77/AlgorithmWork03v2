import java.io.*;

//����
public class Test {

    public static void main(String[] args){
    	//����һ��1-6��������������ȡ�ļ�
    	int dataNumber = 1;// + (int)(Math.random()*6);
	    String fileName = "data/input_assign03_0" + Integer.toString(dataNumber);
    	//read_file("data/input_assign02_0" + Integer.toString(dataNumber) + ".txt");
        HuffmanCompress sample = new HuffmanCompress();
        String inputName = fileName + ".txt";
        String outputName = fileName + ".rar";
        sample.compress(inputName, outputName);
//        File inputFile = new File(file_name + ".txt");
//        File outputFile = new File(file_name + ".rar");
//        sample.compress(inputFile, outputFile);
        
//      File inputFile = new File("gyh.rar");
//      File outputFile = new File("hyq.txt");
//      sample.extract(inputFile, outputFile);
    }
    

    
    
}