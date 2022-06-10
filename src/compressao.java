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

    String[] dicionario = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
        "S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "Ç", "Ã", "Á", "Â",
        "É", "Ê", "Í", "Î", "Ó", "Õ", "Ô", "Ú", "Û", " ", "/", ".", "-" };

    String[] compressaoDicionario = new String[dicionario.length * 3];
    System.arraycopy(dicionario, 0, compressaoDicionario, 0, dicionario.length);
    String valorCompressao = "";
    String letraS = "";
    String proxLetra = "";
    String letraParaSalvarnoDic = "";

    int saveValorRetorno = 0;
    int i = 0;
    boolean podeEncerrar = false;

    while (i < entrada.length() && !podeEncerrar) {

      letraS = "";
      char letra = ' ';
      int valorRetorno = 0;

      letra = entrada.charAt(i);
      letraS += letra;
      valorRetorno = pesquisarNoDicionario(letraS, compressaoDicionario);

      if (valorRetorno != -1) {

        proxLetra += letraS;
        boolean podePesquisar = true;
        char letraProx = ' ';

        while (valorRetorno != -1 && !podeEncerrar) {

          saveValorRetorno = valorRetorno;
          if (i < entrada.length()) {
            if (i == entrada.length() - 1) {

              proxLetra = letraS;
              podePesquisar = false;
              podeEncerrar = true;
              valorRetorno = saveValorRetorno;

            } else {

              letraProx = entrada.charAt(i + 1);
              proxLetra += letraProx;

            }
          }

          if (podePesquisar) {
            valorRetorno = pesquisarNoDicionario(proxLetra, compressaoDicionario);
          }

          if (valorRetorno != -1) {

            letraS = proxLetra;
            i++;

          } else {

            letraParaSalvarnoDic = proxLetra;
            i++;
          }

        }

        if (saveValorRetorno != -1) {
          valorCompressao += saveValorRetorno;

        }

        int posicaoNoDicionario = qtdElementos(compressaoDicionario);
        if (!(letraParaSalvarnoDic.equals(""))) {
          compressaoDicionario[posicaoNoDicionario] = letraParaSalvarnoDic;
        }

        proxLetra = "";
        letraS = "";
        letraParaSalvarnoDic = "";
      } else {
        podeEncerrar = true;
        valorCompressao = "";
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
      String caminhodoArqCompri = "src/database/compressão/futebolCompressao" + entrada + ".db";
      arqPrinci = new RandomAccessFile("src/database/futebol.db", "r");
      arqCompri = new RandomAccessFile(caminhodoArqCompri, "rw");

      boolean marcadorPrimeiros4bytes = false;
      int tamanhodoArquivo4bytesI = 0;
      arqPrinci.seek(2);
      while (arqPrinci.getFilePointer() < arqPrinci.length()) {

        if (!marcadorPrimeiros4bytes) {

          tamanhodoArquivo4bytesI = arqPrinci.readInt();// apagar essa variavel depois pois ela é descessaria
          marcadorPrimeiros4bytes = true;

        }

        // Os sets tem que salvar em byte e nao em string entao nao poe usar a classa
        // indice, pois se não vai salvar com tipo primitivo, tem que fazer um novo
        // tobyteArray fazer dia 10/06

        ft.setIdClube(arqPrinci.readShort());
        ft.setLapide(arqPrinci.readUTF());
        String ftNome = arqPrinci.readUTF().toUpperCase();
        String ftCnpj = arqPrinci.readUTF().toUpperCase();
        String ftCidade = arqPrinci.readUTF().toUpperCase();
        String receberByteComprimidoNome = realizarCompressao(ftNome);
        String receberByteComprimidoCnpj = realizarCompressao(ftCnpj);
        String receberByteComprimidoCidade = realizarCompressao(ftCidade);

        if (!(receberByteComprimidoNome.equals(""))) {
          ft.setNome(receberByteComprimidoNome);
        } else {
          ft.setNome(ftNome);
        }

        boolean podeSetarCnpj = ft.verificaCPNJ(ftCnpj);

        if (!(receberByteComprimidoCnpj.equals("")) && podeSetarCnpj) {
          // ft.setCnpjComprimido(receberByteComprimidoCnpj);
        } else if (podeSetarCnpj) {

          // ft.setCnpj(ftCnpj);

        }

        if (!(receberByteComprimidoCidade.equals(""))) {
          ft.setCidade(receberByteComprimidoCidade);
        } else {
          ft.setCidade(ftCidade);
        }

        ft.setPartidasJogadas(arqPrinci.readByte());
        ft.setPontos(arqPrinci.readByte());

        byte b[];

        b = ft.toByteArray();

        arqCompri.write(tamanhodoArquivo4bytesI);
        arqCompri.write(b);

      }

      arqCompri.close();
      arqPrinci.close();
    } catch (Exception e) {
      System.out.println("Erro no compressaoLze: " + e.getMessage());
    }

  }

}
