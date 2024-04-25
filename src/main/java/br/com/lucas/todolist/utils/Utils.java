package br.com.lucas.todolist.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

public class Utils {

    //Criando um metodo que copia o objeto que está vindo na requisição para o
    //objeto que está indo para o repositorio
    public static void copyNonNullProperties(Object source, Object target) {

        //Metodo copyProperties da classe BeanUtils permite copiar as propriedades
        //de um objeto para outro objeto, o terceiro parametro é a regra que será usada
        BeanUtils.copyProperties(source, target, getNUllPropertyNames(source));
    }

    //Criando um metodo que faz a cópia do objeto no nosso repositório
    //E o que for nulo ele autocompleta para o novo objeto que será criado na requisição

    //Metodo que pega o nome de todas as propriedades nulas
    public static String[] getNUllPropertyNames(Object source){

        //Final: Não pode ser modificado
        //BeanWrapper: Interface do java que acessa as propriedades de um objeto
        //BeanWrapperImpl: Implementação dessa interface
        final BeanWrapper src = new BeanWrapperImpl(source);

        //Gerando um array com todas as propriedades de um objeto
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        //Criando um conjunto de propriedades de valores nulos
        Set<String> emptyNames = new HashSet<>();

        //Fazendo uma iteração para inserir dentro de emptyNames as informações
        for(PropertyDescriptor pd: pds){
            Object srcValue = src.getPropertyValue(pd.getName());
            if(srcValue == null){
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

}
