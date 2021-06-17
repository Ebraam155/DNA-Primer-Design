/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package primer_designerv1;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author 7asa
 */
public class Primer extends Nucleic_Acid {

    public static Vector<Nucleotide> Forward_Primer;
    public static Vector<Nucleotide> Reverse_Primer;
    protected Vector<String> All_Forward;
    protected Vector<String> All_Reverse;
    private float[] bases = new float[4]; //0=>G 1=>C 2=>A 3=>T
    private float percent_digits=0;
    private int best = 0;
    private float[] TM_percent = new float[2];

    public Primer() {
        Forward_Primer = new Vector<>();
        Reverse_Primer = new Vector<>();
        All_Forward = new Vector<>();
        All_Reverse = new Vector<>();
    }

    public void get_all_Forward_Primers(int start_index) {
        int start_index_sequence=0,Number_of_possible_primers=10;
        int counter=0;
        for(int index_counter = start_index_sequence;index_counter<start_index;index_counter++){
            int i=index_counter;
            String checking_primer="";
            String min_length_primer="";
            for(int primer_number = 0;primer_number<Number_of_possible_primers;primer_number++)
            {
                if (primer_number == 0) 
                {   
                    int primer_min_size=18;
                    for(i = index_counter;i<index_counter+primer_min_size;i++)
                    {
                        checking_primer+=Strand.elementAt(i).getType();
                    }
                    min_length_primer = checking_primer;
                }
                else
                {
                    checking_primer = min_length_primer;
                    checking_primer+=Strand.elementAt(i + primer_number).getType();
                    min_length_primer = checking_primer;
                }
                float CG_min=20,CG_max=60,TM_min=57,TM_max=63;
                float[] TM_CG_Check;
                int TM=0,CG=1;
                TM_CG_Check=TM_CGPercent(checking_primer);
                if(TM_CG_Check[TM]>=57 && TM_CG_Check[TM] <= 63){
                    if (TM_CG_Check[CG]>=20 && TM_CG_Check[CG]<=60) {
                        if(checking_primer.charAt(checking_primer.length()-1)=='G'||checking_primer.charAt(checking_primer.length()-1)=='C'){
                            System.out.println(checking_primer);
                            All_Forward.add(checking_primer);       
                            counter++;
                        }
                    }
                }
            }
        }
    System.out.println(counter);
//        for (int i = 0; i < best_forwad.length(); i++) {
//            
//            Nucleotide base = new Nucleotide(best_forwad.charAt(i));
//            Forward_Primer.addElement(base);
//        }
}

public void get_Best_Reverse_Primer() {
        String[] primer = new String[10];
        int count = 0, primer_count = 0;
        for(int i=0;i<10;i++){
            primer[i]="";
        }
        for (int i=Compliment_Strand.size()-1;i>-1 ;i--) {
            if (count < 18) {
                primer[0] += Get_Compliment(Compliment_Strand.elementAt(i).getType());
            } 
            else {
                primer_count++;
                primer[primer_count] = primer[primer_count - 1] + Get_Compliment(Compliment_Strand.elementAt(i).getType());
            }
            count++;
            if (count == 27) {
                break;
            }
        }
        String best_reverse = check_primer(primer);
        for (int i = 0; i < best_reverse.length(); i++) {
            Nucleotide base = new Nucleotide(best_reverse.charAt(i));
            Reverse_Primer.addElement(base);
        }
    }

    private String check_primer(String[] primer) { //Get Best Till you find better
            
            System.out.printf("  "+primer[0]+"  \n");
        for (int count = 0; count < 9; count++) {
            System.out.printf("  "+primer[count+1]+"  \n");
            int end_of_primer=primer[count+1].length()-1;
            if (TM_CGPercent(primer[count])[0] <= TM_CGPercent(primer[count + 1])[0] && TM_CGPercent(primer[count])[0] >= 60) {
                if (TM_CGPercent(primer[count])[1] <= TM_CGPercent(primer[count + 1])[1] && TM_CGPercent(primer[count])[1] >= 50) {
                    if (primer[count].length() <= primer[count + 1].length() && primer[count].length() >= 20) {
                        if(primer[count+1].charAt(end_of_primer)=='G'||primer[count+1].charAt(end_of_primer)=='C')
                            best = count + 1;
                    }
                }
            }
        }
        return primer[best];
    }

    private float[] TM_CGPercent(String primer) {
        bases[0] = 0;
        bases[1] = 0;
        bases[2] = 0;
        bases[3] = 0;
        percent_digits=0;
        for (int i = 0; i < primer.length(); i++) {
            switch (primer.charAt(i)) {
                case 'G':
                    bases[0]++;
                    percent_digits++;
                    break;
                case 'C':
                    bases[1]++;
                    percent_digits++;
                    break;
                case 'A':
                    bases[2]++;
                    break;
                case 'T':
                    bases[3]++;
                    break;
                default:
                    break;
            }
        }
        float size=primer.length();
        TM_percent[0] = 4 * (bases[0] + bases[1]) + 2 * (bases[2] + bases[3]);
        TM_percent[1] = (percent_digits / size) * 100;
        return TM_percent;
    }

    @Override
        public void Sequencing() {
        //Forward Primer Layout
        System.out.print("Forward Primer:\n  ");
        for (Nucleotide i : Forward_Primer) {
            System.out.print("  "+i.getType()+"  ");
        }
        System.out.print("\n");
        System.out.print("5'");
        for(Nucleotide i:Forward_Primer){
            System.out.print("=====");
        }
        System.out.print(" 3'\n");
        
        //Reverse Primer Layout
        System.out.print("Reverse Primer:\n  ");
        for (Nucleotide i : Reverse_Primer) {
            System.out.print("  "+i.getType()+"  ");
        }
        System.out.print("\n");
        System.out.print("5'");
        for(Nucleotide i:Reverse_Primer){
            System.out.print("=====");
        }
        System.out.print(" 3'\n");
    }
    public static String Get_Compliment(String base) {
            switch (base) {
                case "G":
                    return "C";
                case "C":
                    return "G";
                case "A":
                    return "T";
                case "T":
                    return "A";
                default:
                    break;
            }
         return "E";   
        }
    
    
}
