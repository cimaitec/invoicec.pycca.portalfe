package cursojava.anotaciones;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


 
public class CedulaValidator implements ConstraintValidator<cedula, String> {
 
     
    public void initialize(cedula arg0)
    {
    }
 
    public boolean isValid(String cedula, ConstraintValidatorContext context)
    {
    	boolean cedulaCorrecta;
    	try
    	{
    		if (cedula.length() == 10)
    		{
    			int tercerDigito = Integer.parseInt(cedula.substring(2, 3));
    			if (tercerDigito < 6)
    			{
    				// Coeficientes de validaci�n c�dula
    				// El decimo digito se lo considera d�gito verificador
    				int[] coefValCedula = { 2, 1, 2, 1, 2, 1, 2, 1, 2 };
    				int verificador = Integer.parseInt(cedula.substring(9,10));
    				int suma = 0;
    				int digito = 0;
    				for (int i = 0; i < (cedula.length() - 1); i++)
    				{
    					digito = Integer.parseInt(cedula.substring(i, i + 1))* coefValCedula[i];
    					suma += ((digito % 10) + (digito / 10));
    				}
    				
    				if ((suma % 10 == 0) && (suma % 10 == verificador))
    				{
    					cedulaCorrecta = true;
    				}
    				else if ((10 - (suma % 10)) == verificador)
    				{
    					cedulaCorrecta = true;
    				}
    				else
    				{
    					cedulaCorrecta = false;
    				}
    			}
    			else
    			{
    				cedulaCorrecta = false;
    			}
    		}
    		else
    		{
    			cedulaCorrecta = false;
    		}
    	}
    	catch (NumberFormatException nfe)
    	{
    		cedulaCorrecta = false;
    	}catch (Exception err)
    	{
    		System.out.println("Excepcion ocurrio en el proceso");
    		cedulaCorrecta = false;
    	}
    	
    	if (!cedulaCorrecta)
    	{
    		System.out.println("La C�dula ingresada es Incorrecta");
    	}
    	return cedulaCorrecta;
        
    }
}

