package channel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class MyChannel {

    public static void main(String[] args) throws IOException {
        // 파일 생성
        RandomAccessFile file = new RandomAccessFile("nio.txt", "rw");
        // 1. channel 가져옴
        FileChannel channel = file.getChannel();
        // 2. buffer 할당
        ByteBuffer buffer = ByteBuffer.allocate(48);

        // 3. channel이 파일을 buffer로 읽기
        int read = channel.read(buffer);
        System.out.println(read);
        while (read != -1) {
            System.out.println("read " + read);

            // read 모드로 변경
            buffer.flip();
            while (buffer.hasRemaining()) {
                // 한번에 1byte 씩 읽는다
                System.out.println((char) buffer.get());
            }
            // buffer를 읽을 수 있도록 비운다
            buffer.clear();
            read = channel.read(buffer);
            System.out.println(read);
        }
        file.close();
    }
}
