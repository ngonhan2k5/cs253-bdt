package labe;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

public class Pair implements WritableComparable<Pair> {    
    private Text stationId;
    private IntWritable temperature;

    @Override
    public void write(DataOutput out) throws IOException {
        stationId.write(out);
        temperature.write(out);
        
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        stationId.readFields(in);
        temperature.readFields(in);
    }

    @Override
    public String toString() {
        
        return stationId.toString() + "\t" + temperature.toString();
    }

    public Pair(){
        this.stationId = new Text();
        this.temperature = new IntWritable();
    }
    

    public static Pair read(DataInput in) throws IOException {
        Pair w = new Pair();
        w.readFields(in);
        return w;
    }


    @Override
    public int compareTo(Pair o) {
        int ret = this.stationId.compareTo(o.stationId);
        if (ret == 0){
            return this.temperature.compareTo(o.temperature)*-1;
        }else{
            return ret;
        }
    }

    public Text getStationId() {
        return stationId;
    }

    // public void setStationId(Text stationId) {
    //     this.stationId = stationId;
    // }

    public void setStationId(String stationId) {
        this.stationId.set(stationId);;
    }

    public IntWritable getTemperature() {
        return temperature;
    }


    public void setTemperature(int value) {
        this.temperature.set(value);;
    }
  }
