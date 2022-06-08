import java.io.*;

public class criptografia {

  private String chave = "AEDS";

  private byte[] toByteArray(char a[]) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);

    for (int i = 0; i < a.length; i++) {

      dos.writeChar(a[i]);

    }

    return baos.toByteArray();
  }

  private char[] swapArray(char a[]) {

    char letra = a[0];

    for (int i = 0; i < a.length - 1; i++) {

      char shift = a[i + 1];
      a[i] = shift;

    }

    a[a.length - 1] = letra;

    return a;

  }

  private void criarTabelanoArquivodeDados() {

    char letraIndice = 'A';
    char primeiroVetor[] = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
        'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
    byte a[];

    try {

      RandomAccessFile arq = new RandomAccessFile("src/database/criptografia/tabeladeCriptografia.db", "rw");

      for (int i = 0; i < 26; i++) {// Loop tem que ser até 26

        arq.writeChar(letraIndice++);
        a = toByteArray(primeiroVetor);
        arq.write(a);
        primeiroVetor = swapArray(primeiroVetor);
      }

      arq.close();

    } catch (Exception e) {
      System.out.println("Erro no criarTabelanoArquivodeDados: " + e.getMessage());
    }

  }

  private String regularTamanhodaChave(String preRegular, int tamMenor, int op) {

    String retornoRegulada = "";

    if (op == 1) {
      for (int i = 0; i < tamMenor; i++) {

        char letra = preRegular.charAt(i);

        retornoRegulada += letra;

      }
    } else {
      int tamChave = preRegular.length();
      int j = 0;
      char letra;
      for (int i = 0; i < tamMenor; i++) {

        if (i >= tamChave) {

          letra = preRegular.charAt(j);
          j++;

          if (j == tamChave) {
            j = 0;
          }
          retornoRegulada += letra;

        } else {
          letra = preRegular.charAt(i);
          retornoRegulada += letra;
        }

      }

    }

    return retornoRegulada;

  }

  private int retornarPosicaodaLetranoAlfabeto(char letra) {
    char alfabeto[] = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
        'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
    int valorRetorno = -1;

    for (int i = 0; i < 26; i++) {

      if (letra == alfabeto[i]) {

        valorRetorno = i;
        i = 200;

      }

    }
    return valorRetorno;
  }

  private char retornarLetranaTabelaVigenere(char letra, int posi) {
    char letraRetorno = '-';

    posi += 1;

    try {
      RandomAccessFile arq = new RandomAccessFile("src/database/criptografia/tabeladeCriptografia.db", "rw");

      arq.seek(0);
      boolean contador = true;
      char letraIndice = ' ';
      while (contador && (arq.getFilePointer() < arq.length())) {

        letraIndice = arq.readChar();

        if (letraIndice == letra) {

          contador = false;

          arq.seek(arq.getFilePointer() + ((posi * 2) - 2));

          letraRetorno = arq.readChar();

        }

        arq.seek(arq.getFilePointer() + 52);

      }

      arq.close();
    } catch (Exception e) {
      System.out.println("Erro no retornarLetranaTabelaVigenere: " + e.getMessage());
    }
    return letraRetorno;
  }

  public String criptografar(String entrada) {

    String criptografada = "";
    entrada = entrada.toUpperCase();
    boolean temCaracterEspecial = false;

    try {
      RandomAccessFile arq = new RandomAccessFile("src/database/criptografia/tabeladeCriptografia.db", "rw");

      if (arq.length() == 0) {

        criarTabelanoArquivodeDados();
      }

      // primeira coisa é fazer a chave ficar do tamanho da entrada
      int tamChave = chave.length();
      int tamEntrada = entrada.length();
      String chaveCrip = "";
      if (tamChave == tamEntrada) {
        chaveCrip = chave;
      } else if (tamChave > tamEntrada) {

        chaveCrip = regularTamanhodaChave(chave, tamEntrada, 1);

      } else if (tamEntrada > tamChave) {

        chaveCrip = regularTamanhodaChave(chave, tamEntrada, 2);
      }
      // parte acima faz a regulagem da chave para o tamanho da entrada

      // agora tem que fazer a pesquisa na tabela
      // fazer a pesquisa na tabela

      for (int i = 0; i < entrada.length(); i++) {

        char letraEntrada = entrada.charAt(i);
        char letraChave = chaveCrip.charAt(i);

        int posicaodaLetraChave = retornarPosicaodaLetranoAlfabeto(letraChave);

        char letraParaCripto = retornarLetranaTabelaVigenere(letraEntrada, posicaodaLetraChave);

        if (letraParaCripto != '-') {
          criptografada += letraParaCripto;
        } else {
          temCaracterEspecial = true;
          i = 3000;
        }

      }

      arq.close();
    } catch (Exception e) {
      System.out.println("Erro no criptografar: " + e.getMessage());
    }

    if (temCaracterEspecial) {
      criptografada = entrada.toLowerCase();
    }

    return criptografada;
  }

  private char retornarLetraDescriptografada(char letra, int posiChave) {
    char letraRetorno = ' ';
    posiChave += 2;
    boolean contador = true;
    char letraIndice = ' ';
    try {
      RandomAccessFile arq = new RandomAccessFile("src/database/criptografia/tabeladeCriptografia.db", "rw");

      arq.seek(arq.getFilePointer() + ((posiChave * 2) - 2));

      while (contador && (arq.getFilePointer() < arq.length())) {

        letraIndice = arq.readChar();
        if (letra == letraIndice) {
          long salvarPosi = arq.getFilePointer();

          arq.seek((arq.getFilePointer() - (posiChave * 2)));

          letraRetorno = arq.readChar();
          arq.seek(salvarPosi);
          contador = false;
        }

        arq.seek(arq.getFilePointer() + 52);

      }
      arq.close();
    } catch (Exception e) {
      System.out.println("Error no retornarLetraDescriptografada: " + e.getMessage());
    }

    return letraRetorno;
  }

  public String descriptografar(String entrada) {
    String Descriptografada = "";
    entrada = entrada.toUpperCase();
    boolean temCaracterEspecial = false;

    try {
      RandomAccessFile arq = new RandomAccessFile("src/database/criptografia/tabeladeCriptografia.db", "rw");

      if (arq.length() == 0) {

        criarTabelanoArquivodeDados();
      }

      // primeira coisa é fazer a chave ficar do tamanho da entrada
      int tamChave = chave.length();
      int tamEntrada = entrada.length();
      String chaveDescrip = "";

      if (tamChave == tamEntrada) {
        chaveDescrip = chave;
      } else if (tamChave > tamEntrada) {

        chaveDescrip = regularTamanhodaChave(chave, tamEntrada, 1);

      } else if (tamEntrada > tamChave) {

        chaveDescrip = regularTamanhodaChave(chave, tamEntrada, 2);
      }
      // parte acima faz a regulagem da chave para o tamanho da entrada

      // agora tem que fazer a pesquisa na tabela
      // fazer a pesquisa na tabela

      for (int i = 0; i < entrada.length(); i++) {
        char letraEntrada = entrada.charAt(i);
        char letraChave = chaveDescrip.charAt(i);

        int posicaodaLetraChave = retornarPosicaodaLetranoAlfabeto(letraChave);

        char letraParaCripto = retornarLetraDescriptografada(letraEntrada, posicaodaLetraChave);

        if (letraParaCripto != ' ') {
          Descriptografada += letraParaCripto;
        } else {
          temCaracterEspecial = true;
          i = 3000;
        }

      }

      if (temCaracterEspecial) {
        Descriptografada = "";
      }

      arq.close();
    } catch (Exception e) {
      System.out.println("Erro no descriptografar: " + e.getMessage());
    }
    return Descriptografada;
  }
}
