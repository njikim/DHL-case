//  Zhenzheng Jia I6100566     Eunji Kim I6073884        Aline Fattal I6093879
import java.util.*;
import java.io.*;
public class caseproject20
{   private Shipment12 [] shipment_array11; 
    private map12 map; 
    public int numberofshipment; 
    public  ArrayList<double[]>sum_cost_for_route ;
    public int num_feashipment=0;
    public int num_roadshipment=0; 
     //1. constructor
public caseproject20(String filename) 
throws java.io.FileNotFoundException
    {       File file = new File(filename);  
            Scanner input = new Scanner (file);
            numberofshipment = input.nextInt();
            this.shipment_array11 =new Shipment12 [numberofshipment];
            for (int i=0;i<numberofshipment;i++) 
                {  this.shipment_array11[i]= new Shipment12 ();
                   this.shipment_array11[i].dummy_airline=input.nextInt(); 
                   if(this.shipment_array11[i].dummy_airline==0)   // shipment is by airline
                     { this.shipment_array11[i].id=input.nextInt();
                     //this.shipment_array11[i].printing_ofshipements_airline(this.shipment_array11,i);   
                      }
                   else // shipment by road
                     {    this.shipment_array11[i].id=input.nextInt();
                          this.shipment_array11[i].haworg_nbre=input.nextInt()-1;
                          this.shipment_array11[i].dateofshipment=input.nextInt();
                          this.shipment_array11[i].destination_nbre=input.nextInt()-1;
                          this.shipment_array11[i].time_of_shipment=input.nextInt();
                          this.shipment_array11[i].avweight=input.nextDouble();
                          this.shipment_array11[i].date_and_timedueofshipment=input.nextInt();
                          //this.shipment_array11[i].printing_function_ofshipments(this.shipment_array11,i);
                     }
                 }
           input.close();
            String filename11="cities24.txt";
            String filename12="routes27.txt"; 
            map= new map12(filename11,filename12);
        }
 //2. function to find feasible route for ONE shipment 
public boolean feasible_route_one_shipment(int arrival_time,int origin_nbre, int destination_nbre,int time_of_shipment, ArrayList <Integer> roads_array, double average_weight, double track_cost_amount)  
{   if((origin_nbre>this.map.cities.length)|| (destination_nbre>this.map.cities.length)) // checking if origin/destination number is equal to 1000,return false as there is no road existing to this origin or destination. 
    { return false; 
    }
   if(arrival_time > time_of_shipment) 
    { return false;
    }
    if(origin_nbre == destination_nbre) // means arrived at the destination 
    { return true;
    }
     for (int k=0; k<roads_array.size();k++)
          {if (origin_nbre==this.map.roads[roads_array.get(k)].origin_nbre) //checks if the city has been visited already or not 
              {  return false;
              }
           }
    // check constraints 
   // 2.1. checking arrival & departure for all the possible routes 
   int j=0; 
   for( j=0; j<this.map.cities[origin_nbre].departure_list.size();j++) 
   { if( this.map.cities[origin_nbre].departure_list.get(j)[0]>(arrival_time%(10080))) // enough to find one road truck which has a departure time after the arival time of the shipment
        {  break; // the j th road (=ID of the road) found is saved
        }
   } 
   if(this.map.cities[origin_nbre].departure_list.size() == 0)
   { return false;
   }
   int week=(arrival_time/(10080));  // 1440*7=10080
   //2.2. check if there is a direct route from the city we are at 
   for (int k=j; k<this.map.cities[origin_nbre].departure_list.size(); k++)
       { if(this.map.roads[this.map.cities[origin_nbre].departure_list.get(k)[1]].destination_nbre==destination_nbre) //found a direct route from the current origin to the destination
              {   // 1.check for capcity
                  if(this.map.roads[this.map.cities[origin_nbre].departure_list.get(k)[1]].capacity_week[week]> average_weight)
                  {  // update the capacity amount
                     this.map.roads[this.map.cities[origin_nbre].departure_list.get(k)[1]].capacity_week[week]=this.map.roads[this.map.cities[origin_nbre].departure_list.get(k)[1]].capacity_week[week]- average_weight; 
                     // update the cost of the route found 
                     double cost_amount=this.map.roads[this.map.cities[origin_nbre].departure_list.get(k)[1]].cost_per_route(average_weight); // calulates the cost of the smallest route found 
                     track_cost_amount+=(int)cost_amount; 
                     roads_array.add(this.map.cities[origin_nbre].departure_list.get(k)[1]);  // update the roads array with ID number of feasible road found 
                     roads_array.add((int)track_cost_amount); //total cost for this shipment 
                     return true;
                   }
              }
         }
   // since no direct route yet from the origin to the destination city,call the function by recursion.
   //2.3. calulate the costs for the differents routes
      ArrayList<double[]>sum_cost_for_route=new ArrayList <double[]>();
    for (int k=j; k<this.map.cities[origin_nbre].departure_list.size(); k++)
          { 
           double [] cost=new double [2];
           double cost_amount=this.map.roads[this.map.cities[origin_nbre].departure_list.get(k)[1]].cost_per_route(average_weight);
           cost[0]= cost_amount;
           cost[1]= this.map.cities[origin_nbre].departure_list.get(k)[1];
           sum_cost_for_route.add(cost);
         }
       bubblesort(sum_cost_for_route);
   //2.4.check if have enough capaciy in the truck 
   int k=0;
   while(k<sum_cost_for_route.size())
   {
   int smallest_route=(int)sum_cost_for_route.get(k)[1];  // returns the index of the first road of the sorted array
   while(this.map.roads[smallest_route].capacity_week[week]< average_weight)
       {   
         k++; // take the next smallest cost road found since the first road from the sum_cost_for_route array does not satsifies the constraints of the capacity
         if(k>=sum_cost_for_route.size())
         {return false;
         }
         smallest_route= (int)sum_cost_for_route.get(k)[1];
       }
   roads_array.add(smallest_route); // update the array of roads with the ID of the road 
   this.map.roads[smallest_route].capacity_week[week]=this.map.roads[smallest_route].capacity_week[week]- average_weight;// update the capacity amount
   arrival_time=this.map.roads[smallest_route].arrival_time+2*60 + week*10080; // update arrival time of the truck to arrival
   origin_nbre=this.map.roads[smallest_route].destination_nbre; // update the origin with the destination of the truck 
   double cost_amount=this.map.roads[smallest_route].cost_per_route(average_weight); // calulation of the cost of the smallest route found 
   track_cost_amount+=cost_amount; 
   if(feasible_route_one_shipment(arrival_time,origin_nbre,destination_nbre,time_of_shipment,roads_array,average_weight,track_cost_amount))   // apply by recursion to find the feasible route 
   { return true;   
   }
 roads_array.remove(roads_array.size()-1);
 this.map.roads[smallest_route].capacity_week[week]=this.map.roads[smallest_route].capacity_week[week]+ average_weight; // update the capacity amount
 track_cost_amount-=cost_amount; 
   k++;
   }
return false;
}
/* Comment: the recursion of the function will stop at soon as we found a direct route form the origin to the destination, function returns true*/
//3. Sorting algorithm for the feasible route function 
public void bubblesort(ArrayList<double[]>sumcostarray) 
 { for(int currentindex=0; currentindex<sumcostarray.size();currentindex++)
     { for(int i=0;i<sumcostarray.size();i++)
         { if((sumcostarray.get(i)[0] > sumcostarray.get(currentindex)[0])) // comparing the cost values 
                {  double[] temp=new double[2];
                   temp[0]=sumcostarray.get(currentindex)[0];
                    temp[1]=sumcostarray.get(currentindex)[1];
                    sumcostarray.set(currentindex,sumcostarray.get(i));
                    sumcostarray.set(i,temp);
                }
          }
     }
  }
 //4. Sorting algorithm for the feasible optimal route function 
  public void bubblesort_optimal(ArrayList<double[]>sumcostarray)
 { for(int currentindex=0; currentindex<sumcostarray.size();currentindex++)
     { for(int i=0;i<sumcostarray.size();i++)
         {  if((sumcostarray.get(i)[0] > sumcostarray.get(currentindex)[0])) // comparing the cost values 
                {  double[] temp=new double[4];
                   temp[0]=sumcostarray.get(currentindex)[0];
                    temp[1]=sumcostarray.get(currentindex)[1];
                    temp[2]=sumcostarray.get(currentindex)[2];
                    temp[3]=sumcostarray.get(currentindex)[3];
                    temp[4]=sumcostarray.get(currentindex)[4];
                    sumcostarray.set(currentindex,sumcostarray.get(i));
                    sumcostarray.set(i,temp);
                }
          }
     }
  }
// 5.function to find feasible optimal route for ONE shipment 
 ArrayList<double[]>candidate_list=new ArrayList <double[]>();
 public boolean optimizing_current_feasibleroutes(int arrival_time,int time_of_shipment, ArrayList <Integer> roads_array, double average_weight)
 { int week=(arrival_time/(10080)); 
   //5.1.The case if there is no road before the starting city ( first index of the road array)
   int i=0; 
   int g=0;
   for( g=0; g<this.map.cities[this.map.roads[roads_array.get(i)].origin_nbre].departure_list.size(); g++) 
       { if(this.map.cities[this.map.roads[roads_array.get(i)].origin_nbre].departure_list.get(g)[0]>(arrival_time%10080)) 
         { break; //g th road satisfying the arrvial time
         }
       }
  for(int e = g;e<this.map.cities[this.map.roads[roads_array.get(i)].origin_nbre].departure_list.size();e++)
       { if(this.map.roads[this.map.cities[this.map.roads[roads_array.get(i)].origin_nbre].departure_list.get(e)[1]].capacity_week[week]> average_weight)
            {  for(int h = i+1;h<roads_array.size()-2;h++) // taking the next road index possible
                { if(this.map.roads[this.map.cities[this.map.roads[roads_array.get(i)].origin_nbre].departure_list.get(e)[1]].destination_nbre == this.map.roads[roads_array.get(h)].destination_nbre) //  h is the index checking for the destination value 
                            {       double [] cost=new double [5];
                                    double new_cost_amount= this.map.roads[this.map.cities[this.map.roads[roads_array.get(i)].origin_nbre].departure_list.get(e)[1]].cost_per_route(average_weight);
                                    double current_cost_amount=0;
                                    int c=i; 
                                    int count=0; 
                                    while(roads_array.get(c)!=roads_array.get(h))
                                     { current_cost_amount+= this.map.roads[roads_array.get(c)].cost_per_route(average_weight);
                                       count++; 
                                      }
                                    c++;
                                    count++;
                                    current_cost_amount+= this.map.roads[roads_array.get(h)].cost_per_route(average_weight);
                                    double difference_cost=new_cost_amount-current_cost_amount; 
                                    cost[0]=difference_cost ; //difference in the cost between the current cost and the new feasible cost. 
                                    cost[1]= this.map.cities[this.map.roads[roads_array.get(i)].origin_nbre].departure_list.get(e)[1]; // input the index of the road 
                                    cost[2]=i;  // saving the ith position in roads_array which corresponds to the position at which the new possible optimal solution needs to be replaced. 
                                    cost[3]= new_cost_amount;
                                    cost[4]=count; 
                                    candidate_list.add(cost); 
                              }
                       }
              }
    }
 //5.1.The case chaking for all the possible intermediate optimal routes 
  for(i=1; i<roads_array.size()-2;i++) 
   {   int k=0;  // origin is for the road array
       for( k=0; k<this.map.cities[this.map.roads[roads_array.get(i)].origin_nbre].departure_list.size(); k++) 
       {if(this.map.cities[this.map.roads[roads_array.get(i)].origin_nbre].departure_list.get(k)[0]>(this.map.roads[roads_array.get(i-1)].arrival_time%10080)) 
         { break; //g th road satisfying the arrvial time
         }
       }  
     for(int e = k;e<this.map.cities[this.map.roads[roads_array.get(i)].origin_nbre].departure_list.size();e++)
       { if(this.map.roads[this.map.cities[this.map.roads[roads_array.get(i)].origin_nbre].departure_list.get(e)[1]].capacity_week[week]> average_weight)
            { for(int h = i+1;h<roads_array.size()-1;h++) 
                { if(this.map.roads[this.map.cities[this.map.roads[roads_array.get(i)].origin_nbre].departure_list.get(e)[1]].destination_nbre == this.map.roads[roads_array.get(h)].destination_nbre) //  h is the index checking for the destination value 
                    {     if(this.map.roads[this.map.cities[this.map.roads[roads_array.get(i)].origin_nbre].departure_list.get(e)[1]].arrival_time < time_of_shipment)
                           {        double [] cost=new double [5];
                                    double new_cost_amount= this.map.roads[this.map.cities[this.map.roads[roads_array.get(i)].origin_nbre].departure_list.get(e)[1]].cost_per_route(average_weight);
                                    double current_cost_amount=0;
                                    int c=i; 
                                    int count=0; 
                                    while(c != h)
                                    { current_cost_amount+= this.map.roads[roads_array.get(c)].cost_per_route(average_weight);
                                      count++; 
                                     }
                                    c++;
                                    count++;
                                    current_cost_amount+= this.map.roads[roads_array.get(h)].cost_per_route(average_weight);
                                    double difference_cost=current_cost_amount-new_cost_amount; 
                                    cost[0]=difference_cost ;  
                                    cost[1]= this.map.cities[this.map.roads[roads_array.get(i)].origin_nbre].departure_list.get(e)[1]; 
                                    cost[2]=i; 
                                    cost[3]= new_cost_amount;
                                    cost[4]=count; 
                                    candidate_list.add(cost);
                             }
                        }
                    }
                }
            }
         }
         if (candidate_list.size()==0)
         {    return false;
         }  
        else
         {bubblesort_optimal(candidate_list);
          if(candidate_list.get(candidate_list.size()-1)[0]>0) // checking if the cost was a positive value of the last element in the sorted ArrayList  
           { // need to delete the current feasible solution form the roads_array such that it will not be considered in the next iteration. 
             for( int k=0; k<=(int)candidate_list.get(candidate_list.size()-1)[4];k++)
             {  this.map.roads[(int)candidate_list.get(candidate_list.size()-1)[2]].capacity_week[week]=this.map.roads[(int)candidate_list.get(candidate_list.size()-1)[2]].capacity_week[week]+average_weight; // update the capacity of the deleted roads
                roads_array.remove((int)candidate_list.get(candidate_list.size()-1)[2]); 
             }
              roads_array.add((int)candidate_list.get(candidate_list.size()-1)[2],(int) candidate_list.get(candidate_list.size()-1)[1]); // adding the index of the road (saved as index_new) at ith position 
             // update the capacity 
              this.map.roads[(int)candidate_list.get(candidate_list.size()-1)[1]].capacity_week[week]=this.map.roads[(int)candidate_list.get(candidate_list.size()-1)[1]].capacity_week[week]-average_weight;
              optimizing_current_feasibleroutes(arrival_time,time_of_shipment,roads_array,average_weight);  // call the function by recursion  
              return true;                                                                                                                         
           }
         }
     return false; // recursion is stopped
 }   
// 6.function to find the feasible routes for all the shipments 
public void feasible_route_forallshipments(String s[][]) 
{ int k=0;
  int currentday = 0;
  double starttime = 0;
  while (k<numberofshipment) 
  { if(currentday != this.shipment_array11[k].dateofshipment)
      { //print the day 
       double temp = System.currentTimeMillis()-starttime;
       System.out.println("the day is "+" "+ currentday + "and the number of shipments is "+ k + " and the time is " + temp +"ms" );
       System.out.println("\n");
       currentday = this.shipment_array11[k].dateofshipment;
       starttime = System.currentTimeMillis();
      }
     System.out.print("ID:"+ this.shipment_array11[k].id + ":"); // printing the ID of the road
     int a= this.shipment_array11[k].id;
     s[k+2][0]=new String(String.valueOf(a));
 
     if(this.shipment_array11[k].check_shipmenttype()==false)
      {System.out.println("This shipment cannot be shipped by roads trucks.");
       s[k+2][3]=new String("No");
      }
      else // can be shipped
      { s[k+2][3]=new String("Yes"); 
        num_roadshipment+=1;
        int time_due=this.shipment_array11[k].date_and_timedueofshipment+6*60; // to have it in minutes the date of the shipment plus the time of shipment and needs to be at station at 6 heures
         int starting_time=this.shipment_array11[k].dateofshipment*24*60+18*60; // start at 18h on the date of shipment
         int origin_nbre=this.shipment_array11[k].haworg_nbre;
         int destination_nbre=this.shipment_array11[k].destination_nbre;
         int trackcostamount=0;
         ArrayList<Integer>roads= new ArrayList<Integer>();
         double average_weight=this.shipment_array11[k].avweight;
         int size1=0;
         int size2=0;
         if(feasible_route_one_shipment(starting_time,origin_nbre,destination_nbre,time_due,roads,average_weight,trackcostamount))
         {   if(roads.size()==0)
             { System.out.println("No shipment needed." ); // The origin city equals the destination city. 
               s[k+2][1]=new String("No");
               s[k+2][5]=new String("0");
             }
         else
         {   
           size1= roads.size()-1;
           s[k+2][1]=new String("Yes");
         }
         if(roads.size()>0){ 
           if(optimizing_current_feasibleroutes(starting_time,time_due,roads,average_weight)==false){ 
             s[k+2][2]=new String("Yes");
             num_feashipment+=1;
             System.out.print("optimal route:");
             for(int i=0;i<roads.size()-1;i++)
             {
               System.out.print(roads.get(i)+" "); 
               int b=roads.get(i);
               if(i==0){
                 s[k+2][4]=new String(String.valueOf(b));}
               else{
                 s[k+2][4]=new String(s[k+2][4]+","+String.valueOf(b));
               }}
                 System.out.println(",cost:"+ roads.get(roads.size()-1));
                 int c=roads.get(roads.size()-1);
                 s[k+2][5]=new String(String.valueOf(c));
                 size2= roads.size()-1;
                    }
                }
         } 
         else
         { System.out.println("No fesible route.");
           s[k+2][2]=new String("No");
         }
       }
      k++;
     }
 }
public void output(String s[][])throws IOException{    
  s[0][0]=new String("Total shipments is " + numberofshipment);  
  s[0][1]=new String("Total shipments can be transported by road is " + num_roadshipment);  
  s[0][2]=new String("Total shipments can be shiped is " + num_feashipment);
  s[1][0]=new String("ID");
  s[1][1]=new String("need shipment");
  s[1][2]=new String("Feasible route");
  s[1][3]=new String("Shiped by road");
  s[1][4]=new String("optimal route");
  s[1][5]=new String("Cost");
  
  File file = new File("output.txt");
  FileWriter out = new FileWriter(file);
  for(int i=0;i<numberofshipment+2;i++){
    for(int j=0;j<6;j++){
      out.write(s[i][j]+"\t");}
    out.write("\r\n");}
  out.close();
} 
public static void main (String args[])throws IOException 
  {   String filename10="shipment27.txt";     
      File file = new File(filename10);  
      Scanner input = new Scanner (file);
      int n = input.nextInt();
      String s[][]=new String[n+2][6];
     try
    {  caseproject20 caseproject1112= new caseproject20(filename10);
        double startTime = System.currentTimeMillis();
        caseproject1112.feasible_route_forallshipments(s);
        double endTime = System.currentTimeMillis(); 
        System.out.println("The total running time is:"+ (endTime - startTime) + " ms");
        caseproject1112.output(s);
    }
    catch (FileNotFoundException e)
    {  
      e.printStackTrace();
    }
    
  }
}