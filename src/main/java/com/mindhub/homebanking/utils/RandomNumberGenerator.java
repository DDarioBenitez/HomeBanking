package com.mindhub.homebanking.utils;

import java.util.Random;
import java.util.StringJoiner;

public final class RandomNumberGenerator {
    public static String accountNumberGenerator(){// crea un numero random
        // Genera un número random entre 0 (incluido) y 1 (excluido)
        double random = Math.random();
        // Multiplica el número random por 10^8 para obtener 8 dígitos decimales
        random *= 100000000;
        // Convierte el resultado en un número entero redondeando hacia abajo
        int entero = (int) Math.floor(random);
        // Convierte el número entero en una cadena de texto
        StringBuilder cadena = new StringBuilder(Integer.toString(entero));
        // Si la cadena tiene menos de 8 dígitos, agrega ceros a la izquierda hasta que tenga 8 dígitos
        while (cadena.length() < 8) {
            cadena.insert(0, "0");
        }
        return cadena.toString();
    }

    public static int cvvGenerator(){
        Random random= new Random(); //Creamos una instancia de la clase Random.

        return random.nextInt(900)+100; // Usamos el metodo nextInt de la Clase Random en la instancia random, este genera un numero aleatorio de 0(inclucive) y el 900(es el limite osea que los numeros van a ser de 0-899) y le sumamos 100 para llegar a un rando de 100-999
    }

    public static String debitNumberGenerator(){
        StringBuilder debitSection= new StringBuilder("4000"); // Creamos una variable y del tipo StringBuilder y instanciamos un objeto pasandole por parametro la primera parte del numero en este caso 4000 que hace referencia a debito
        Random random= new Random();//Creamos una variable del tipo Random instanciamos un objeto de la clase Random
        for (int i = 0; i < 3; i++){ //iniciamos un bucle for iniciando en 0 que va a repetirse hasta que i sea menos que 3 (osea 2)
            int section = random.nextInt(10000);// usamos nuevamente el metodo nextInt de la clase Random pasandole como limite 9999(osea dara valores de 0 a 9999)
            String stringSection= String.format("%04d",section); //le damos formato de string al numero que generamos en la linea anterior y si este es menor de 4 digitos le agrega 0 adelante hasta que sean 4 digitos, %es el indicador que estamos declarando un argumento para ser formatear, 04 indica que se deben usar 4 caracteres y en caso de que sean menos se agregan 0 por eso el 0 esta antes que el 4, d indica que es un numero entero
            debitSection.append(" ").append(section);// utilizamos el metodo append de StringBuilder para contaquetar debitsection con los numeros que estamos generando aleatoriamente, primero que concatenamos un espacio y luego el numero generado.
        }
        return debitSection.toString();// retornamos el numero creado con las 4 secciones separadas por espacios pero el metodo se decalro como un de tipo String por eso lo pasamos String con el metodo toString de StringBuilder
    }

    public static String creditNumberGenerator(){
        StringJoiner creditNumber= new StringJoiner(" ");//Hacemos lo miso que el metodo debitNumberGeneratos la diferencia es que usamos la clase StringJoiner para instanciar un objeto de dicha clase en la variable creditNumber, esta clase es diferente por que en lugar de pasarle la primera parte del numero le pasamos el caracter con el que lo seccionaremos
        Random random= new Random(); // idem a debitGenerator
        int count=0;// declaramos un contador para controlar las repeticiones del bucle doWhile
        do{ //iniciamos un bucle doWhile(a diferencia del while siempre se ejecuta minimo 1 vez)
            int section= random.nextInt(10000); //idem al metodo debitNumberGenerator
            String stringSection= String.format("%04d", section);//idem al metodo debitNumberGenerator
            creditNumber.add(stringSection);// en este caso usamos el metodo add que es de StringJoiner para añadir el numero generado, como especificamos arriba que se delimitan con un espacio no nesecitamos agregar nada mas
            count++;// le sumamos uno al contador
        }while(count<3);// una vez el contador llegue a 3 el bucle no se volvera a repetir esto quiere decir que cuando se repita 3 veces pasa a la siguiente linea

        return "4513"+ " " +creditNumber.toString();// a dfirencia del metodo debitNumberGenerator tenemos que agregar el numero por defecto para identificar que es de credito y concatenar el numero creado y pasarlo a string como el metodo ya mencionado
    }
}
