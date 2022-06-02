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

  public void criarTabelanoArquivodeDados() {

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

}
