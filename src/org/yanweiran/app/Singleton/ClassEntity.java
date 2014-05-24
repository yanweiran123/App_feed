package org.yanweiran.app.Singleton;

/**
 * Created by lenov on 14-2-25.
 */
public class ClassEntity  {

    private String className;
    private int classNew;
    private String classId;

    public void setClassName(String className)
        {
            this.className = className;
        }
    public void setClassId(String classId)
    {
        this.classId = classId;
    }
    public void setClassNew(int classNew)
    {
        this.classNew =classNew;
    }
    public String getClassName()
    {
        return  className;
    }
    public int getClassNew()
    {
        return classNew;
    }
    public String getClassId(){return classId;}
}
