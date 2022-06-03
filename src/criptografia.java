import java.io.*;

public class criptografia {

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

      for (int i = 0; i < 26; i++) {

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

  public void criptografar(String entrada) {

    String chave = "aeds";

    try {
      RandomAccessFile arq = new RandomAccessFile("src/database/criptografia/tabeladeCriptografia.db", "rw");

      if (arq.length() == 0) {

        criarTabelanoArquivodeDados();
      }
      // primeira coisa é fazer a chave ficar do tamanho da entrada
      int tamChave = chave.length();
      int tamEntrada = entrada.length();
      if (tamChave == tamEntrada) {

      } else if (tamChave > tamEntrada) {

        regularTamanhodaChave(chave, tamEntrada, 1);

      } else if (tamEntrada > tamChave) {

        regularTamanhodaChave(chave, tamEntrada, 2);
      }
      // parte acima faz a regulagem da chave para o tamanho da entrada

      // agora tem que fazer a pesquisa na tabela
      // fazer a pesquisa na tabela

      arq.close();
    } catch (Exception e) {
      System.out.println("Erro no criptografar: " + e.getMessage());
    }

  }

}
