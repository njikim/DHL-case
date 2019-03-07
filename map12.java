import java.util.*;
import java.io.*;
//  Zhenzheng Jia I6100566     Eunji Kim I6073884        Aline Fattal I6093879
public class map12
{   public city12 [] cities; 
    public Road12 [] roads; 
    public int numberofroads;
    private int numberofcities;
    private int numberoforigrincities;
    private int numberofdestinationcities;
public map12(String filename1, String filename) 
throws java.io.FileNotFoundException
    {     // cities values
        {   File file1 = new File(filename1);  
            Scanner input1 = new Scanner (file1);
            this.numberofcities = input1.nextInt();
            cities =new city12 [this.numberofcities];
             String name; // name of the city 
             for (int i=0;i<this.numberofcities;i++)
             {  name=input1.nextLine();    
                cities[i]= new city12(i,name); 
             }
           input1.close();
        }
       {    File file = new File(filename); 
            Scanner input = new Scanner (file);
            numberofroads = input.nextInt();
            roads =new Road12 [numberofroads];
            roads =new Road12 [numberofroads];
            double [] capacity_week= new double [2];
         for (int i=0;i<numberofroads;i++)
            {   int date =input.nextInt();
                int origin_nbre=input.nextInt();
                origin_nbre-=2;
                int departure_time=input.nextInt(); 
                int destination_nbre=input.nextInt();
                destination_nbre-=2;
                int arrival_time=input.nextInt(); 
                double max_cap=input.nextDouble();
                double cost= input.nextDouble();  
                int  unit_cost=input.nextInt();
                roads[i]=new Road12(date,origin_nbre,departure_time, destination_nbre,  arrival_time, max_cap,cost,unit_cost,i);
                for( int k=0; k< 2; k++)
                 { roads[i].capacity_week[k]=roads[i].max_cap;
                 }
               roads[i].put_departure(cities);  // initialize the departure_list
                }
           input.close();
        }
   }
}