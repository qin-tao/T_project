package tao.trading.strategy;

import org.netbeans.jemmy.ClassReference;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JDialogOperator;
import org.netbeans.jemmy.operators.JFrameOperator;
import org.netbeans.jemmy.operators.JPasswordFieldOperator;
import org.netbeans.jemmy.operators.JTextFieldOperator;

public class TWSSupervisor
{
    public TWSSupervisor (String userid, String password)
    {
        try
        {
            String [] params = new String [1];
            //params [0] = "~/IBJts";   // Directory where TWS is installed
            params [0] = "c:/Jts";   // Directory where TWS is installed
            //new ClassReference("jclient.LoginFrame").startApplication(params);
            new ClassReference("jclient.Launcher").startApplication(params);

            JFrameOperator loginFrame = new JFrameOperator("Login");
            JTextFieldOperator userNameField = new org.netbeans.jemmy.operators.JTextFieldOperator (loginFrame);
            JPasswordFieldOperator passwordField = new org.netbeans.jemmy.operators.JPasswordFieldOperator (loginFrame);

            JButtonOperator loginButton = new JButtonOperator (loginFrame, "Login");

            loginFrame.requestFocus ();
            userNameField.requestFocus ();
            userNameField.typeText (userid);
            passwordField.requestFocus ();
            passwordField.typeText (password);
            loginButton.requestFocus ();
            loginButton.push ();
            
            Thread.sleep(30000);

            JDialogOperator newJDAOp = new JDialogOperator("Warning"); // NOI18N
            new JButtonOperator(newJDAOp, "I understand and accept").push(); // NOI18N
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
		String userid = args[0];
		String password = args[1];

        new TWSSupervisor (userid, password);
    }
}
