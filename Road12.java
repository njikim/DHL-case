//  Zhenzheng Jia I6100566     Eunji Kim I6073884        Aline Fattal I6093879
import java.util.*;
import java.io.*;
public class Road12 
{   public int date;
    public  int origin_nbre;   
    public  int departure_time;
    public  int destination_nbre;  
    public  int arrival_time;
    public double max_cap;
    public double cost;
    public int unit_cost; 
    public int id_road; 
    public double [] capacity_week;  
  public Road12 (int date1, int origin_nbre1,int departure_time1, int destination_nbre1,int arrival_time1,double max_cap1,double cost1, int unit_cost1, int id_road11)
  {  date=date1;
     origin_nbre=origin_nbre1; 
     departure_time=departure_time1;
     destination_nbre=destination_nbre1;
     arrival_time=arrival_time1;
     max_cap=max_cap1;
     cost= cost1;
     unit_cost=unit_cost1;
     id_road=id_road11;
     capacity_week=new double [2];
 }  
public double cost_per_route(double avweight)
{  double costforroute=0; 
    if (unit_cost==1)
   { costforroute = cost*avweight; 
   }
   else 
   {costforroute = cost; 
   }
  return costforroute; 
}    
 // function which puts all the roads departuring from the specific city with the departure time and the ID number of the road
 public void put_departure(city12 []cities) 
  {  int [] departure=new int [2];
     for(int i =0; i< cities[origin_nbre].departure_list.size();i++)  
     {  if( departure_time < cities[origin_nbre].departure_list.get(i)[0]) 
         { departure[0]= departure_time;      
          departure[1]= id_road;
          cities[origin_nbre].departure_list.add(i,departure); 
           return;
         }
     }
     {  departure[0]= departure_time;      
        departure[1]= id_road;
        cities[origin_nbre].departure_list.add(departure);
     }
  }
}

