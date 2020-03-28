package lab3;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class Pair implements Writable {    
    private int sum;
    private int count;

    public void write(DataOutput out) throws IOException {
      out.writeInt(sum);
      out.writeInt(count);
    }

    public void readFields(DataInput in) throws IOException {
        sum = in.readInt();
        count = in.readInt();
    }

    public Pair(){
        
    }

    public Pair(int sum, int count){
        this.count = count;
        this.sum = sum;
    }
    public void set(int sum, int count){
        this.count = count;
        this.sum = sum;
    }

    public static Pair read(DataInput in) throws IOException {
        Pair w = new Pair();
        w.readFields(in);
        return w;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
  }
// public class Pair implements Writable {    
//     private int sum;
//     private int count;

//     public void write(DataOutput out) throws IOException {
//       out.writeInt(sum);
//       out.writeInt(count);
//     }

//     public void readFields(DataInput in) throws IOException {
//         sum = in.readInt();
//         count = in.readInt();
//     }

//     public static Pair read(DataInput in) throws IOException {
//         Pair w = new Pair();
//         w.readFields(in);
//         return w;
//     }
//   }