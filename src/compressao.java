import java.io.RandomAccessFile;

public class compressao {

  private int qtdElementos(String[] a) {

    int contador = 0;

    for (int i = 0; i < a.length; i++) {

      if (a[i] != null) {
        contador++;
      } else {
        i = a.length;
      }

    }

    return contador;
  }

  private int pesquisarNoDicionario(String letraS, String[] dicionarioAtualizando) {

    int posiLetra = -1;

    for (int j = 0; j < dicionarioAtualizando.length; j++) {

      if (letraS.equals(dicionarioAtualizando[j])) {

        posiLetra = j;

      } else if (dicionarioAtualizando[j] == null) {
        j = dicionarioAtualizando.length;
      }

    }

    return posiLetra;

  }

  private String realizarCompressao(String entrada) {

    // Ele ja incrementa no dicionario perfeitamente, só falta agora incrementar
    // quando acha um novo número no dicionario como um de 3 letras

    criptografia crpi = new criptografia();

    entrada = crpi.descriptografar(entrada);// retirar essa crip depois

    String[] dicionario = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
        "S", "T", "U", "V", "W", "X", "Y", "Z", "1", "2", "3", "4", "5", "6", "7", "8", "9", "Ç", "Ã", "Á", "Â",
        "É", "Ê", "Í", "Î", "Ó", "Õ", "Ô", "Ú", "Û", " " };

    String[] compressaoDicionario = new String[dicionario.length * 3];
    System.arraycopy(dicionario, 0, compressaoDicionario, 0, dicionario.length);
    String valorCompressao = "";
    String letraS = "";
    String proxLetra = "";
    int contadorProxLetra = 1;
    int saveValorRetorno = 0;
    // fim das variaveis do método realizar compressaoDicionario

    for (int i = 0; i < entrada.length(); i++) {
      letraS = "";
      char letra = entrada.charAt(i);
      letraS += letra;
      contadorProxLetra = 1;
      int valorRetorno = pesquisarNoDicionario(letraS, compressaoDicionario);

      if (valorRetorno != -1) {

        proxLetra += letraS;
        boolean podePesquisar = true;
        int iSave = i;
        boolean podeMudaroI = true;

        while (valorRetorno != -1) {
          saveValorRetorno = valorRetorno;

          if (i < entrada.length() - 1) {
            char letraProx = entrada.charAt(i + contadorProxLetra);
            proxLetra += letraProx;
          } else if (i == entrada.length() - 1) {
            valorRetorno = -1;
            podePesquisar = false;
            podeMudaroI = false;

          }

          if (podePesquisar) {
            valorRetorno = pesquisarNoDicionario(proxLetra, compressaoDicionario);
          }

          if (valorRetorno != -1) {
            letraS += proxLetra;

          }

          contadorProxLetra++;
          i++;
        }

        if (saveValorRetorno != -1) {
          valorCompressao += saveValorRetorno;
          if (podeMudaroI) {
            i = iSave;
          }

        }

        int posicaoNoDicionario = qtdElementos(compressaoDicionario);

        compressaoDicionario[posicaoNoDicionario] = proxLetra;

        proxLetra = "";
        letraS = "";
      }

    }
    return valorCompressao;
  }

  public void compressaoLzw(int entrada) {

    // tem que cromprimir nome, cnpj, cidade
    // ARRAYDEBYTE(ID+LAPIDE+NOME+CNPJ+CIDADE+PARTIDASJOGADAS+PONTOS)modelo de cada
    // registro no arquivo principal.

    // variaveis do método realizar compressao

    RandomAccessFile arqPrinci;
    RandomAccessFile arqCompri;
    fut ft = new fut();

    try {
      String caminhodoArqCompri = "src/database/compressão/futebolCompressao" + entrada;
      arqPrinci = new RandomAccessFile("src/database/futebol.db", "rw");
      // arqCompri = new RandomAccessFile(caminhodoArqCompri, "rw");

      boolean marcadorPrimeiros4bytes = false;
      int tamanhodoArquivo4bytesI = 0;
      arqPrinci.seek(2);
      while (arqPrinci.getFilePointer() < arqPrinci.length()) {

        if (!marcadorPrimeiros4bytes) {

          tamanhodoArquivo4bytesI = arqPrinci.readInt();// apagar essa variavel depois pois ela é descessaria
          marcadorPrimeiros4bytes = true;

        }

        ft.setIdClube(arqPrinci.readShort());
        ft.setLapide(arqPrinci.readUTF());
        String receberByteComprimidoNome = realizarCompressao(arqPrinci.readUTF().toUpperCase());
        String receberByteComprimidoCnpj = realizarCompressao(arqPrinci.readUTF().toUpperCase());
        String receberByteComprimidoCidade = realizarCompressao(arqPrinci.readUTF().toUpperCase());

        if (!(receberByteComprimidoNome.equals(""))) {
          ft.setNome(receberByteComprimidoNome);
        }

        if (!(receberByteComprimidoCnpj.equals(""))) {
          ft.setCnpj(receberByteComprimidoCnpj);
        }

        if (!(receberByteComprimidoCidade.equals(""))) {
          ft.setCidade(receberByteComprimidoCidade);
        }

      }

      // arqCompri.close();
      arqPrinci.close();
    } catch (Exception e) {
      System.out.println("Erro no compressaoLze: " + e.getMessage());
    }

  }

}
