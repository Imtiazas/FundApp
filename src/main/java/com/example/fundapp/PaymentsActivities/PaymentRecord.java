package com.example.fundapp;

public class PaymentRecord {

    public int findPosition(String value, String[] arr)
    {
        int pos=0;
        for(int i=0;i<arr.length;i++)
        {
            if(value.equals(arr[i]))
            {
              pos=i;
            }
        }
        return pos;
    }

    public String[] setUpArray(int pos,String[] arr)
    {
        int j=0;
        String[] newArray= new String[arr.length-pos];
        for(int i=pos;i<arr.length;i++)
        {
            newArray[j]=arr[i];
            j++;
        }
        return newArray;
    }

    public void showArray(String[] arr)
    {
        for(int i=0;i<arr.length;i++)
        {
            System.out.println("$$$$ index is"+i+" value = " + arr[i]);

        }

    }
}
