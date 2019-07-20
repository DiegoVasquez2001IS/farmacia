package sesion;

import usuarios.model.Usuario;

/**
 * Created by user on 5/06/2017.
 */
public class Sesion {

    private static Usuario mUsuarioActual = null;

    public static void setmUsuarioActual(Usuario usuario){
        mUsuarioActual = usuario;
    }

    public static Usuario getmUsuarioActual(){
        return mUsuarioActual;
    }

}
