/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ssii2009.mips.architectures;

/**
 *
 * @author Jose
 */
public class SignalsValuesAndIntervals {

    private String name;
    private String[] values;
    private String[] intervals;
    private int size;
    private int cursor;

    public SignalsValuesAndIntervals(String name, int size){
        this.name = name;
        this.values = new String[size];
        this.intervals = new String[size];
        this.size = size;
        for (int i = 0; i<this.size; i++){
            values[i] = null;
        }
        for (int j=0; j<this.size; j++){
            intervals[j] = null;
        }
        
        this.cursor = 0;

    }

    public void addValueAndInterval(String value, String interval){

        if (cursor < this.size){
            values[cursor] =  value;
            intervals[cursor] = interval;
            cursor++;
        }

    }

    public String getName(){
        return this.name;
    }

    public String[] getValues(){
        return this.values;
    }

    public String getValueI(int i){
        if (i<this.cursor)
            return this.values[i];
        return "";
    }

    public String[] getIntervals(){
        return this.intervals;
    }

    public String getIntervalI(int i){
        if (i<this.cursor)
            return this.intervals[i];
        return "";
    }

    public int numberOfIntervals(){
        int num = 0;
        int i=0;
        while (intervals[i] != null){
            i++;
            num++;
        }
        return num;
    }

    public int getCursor(){
        return this.cursor;
    }

}
