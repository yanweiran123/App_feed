package org.yanweiran.app.Singleton;

import java.io.Serializable;

/**
 * Created by lenov on 14-2-7.
 */
public class RegisterPerson  implements Serializable {
            private String  name;
            private String password;
            private String babyName;

            public  RegisterPerson(String name,String password,String babyName)
            {
                this.name = name;
                this.password = password;
                this.babyName = babyName;
            }

           public    String getEmail()
            {
                return  name;
            }
    public   String getPassword()
            {
                return  password;
            }
    public    String getBabyName()
            {
                return  babyName;
            }
}
