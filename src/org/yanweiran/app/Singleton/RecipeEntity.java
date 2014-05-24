package org.yanweiran.app.Singleton;

import java.io.Serializable;

/**
 * Created by lenov on 14-2-22.
 */
public class RecipeEntity implements Serializable {


    private  String can1;
    private String can2;
    private String can3;
    private String can4;
    private String can5;


    public void setCan1(String can1)
    {
        this.can1 = can1;
    }
    public void setCan2(String can2)
    {
        this.can2 = can2;
    }
    public  void setCan3( String can3)
    {
        this.can3 = can3;
    }
    public void setCan4(String can4)
    {
        this.can4 = can4;
    }
    public void setCan5(String can5)
    {
        this.can5=can5;
    }
    public String getCan1()
    {
        return  can1;
    }
    public String getCan2()
    {
        return  can2;
    }
    public String getCan3()
    {
        return  can3;
    }
    public String getCan4()
    {
        return  can4;
    }
    public  String getCan5()
    {
        return  can5;
    }
}
