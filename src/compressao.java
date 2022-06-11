import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class compressao {

  // --------------------X-Métodos-de-ajuda-Compressao-X--------------------//

  private void deletaTudo(String caminho) {

    try {
      PrintWriter writer = new PrintWriter(caminho);
      writer.print("");
      writer.close();
    } catch (Exception e) {
      System.out.println("Erro no método deletaTudoCompressao: " + e.getMessage());
    }

  }

  private int qtdElementosString(String[] a) {

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

  private int qtdElementosInt(int[] a) {
    int contador = 0;
    for (int i = 0; i < a.length; i++) {

      if (a[i] != -1) {
        contador++;
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

  private int[] inicializarVetor(int[] s) {

    for (int i = 0; i < s.length; i++) {

      s[i] = -1;

    }

    return s;
  }

  private boolean vetorEstaVazio(int[] s) {

    boolean teste = false;

    if (s[0] == -1) {
      teste = true;
    }
    return teste;
  }

  private byte[] deIntParaByteArray(int[] s) {

    byte[] a = new byte[s.length];

    for (int i = 0; i < s.length; i++) {

      a[i] = (byte) s[i];

    }
    return a;
  }

  // --------------------X-Fim-Métodos-de-ajuda-Compressao-X--------------------//

  // --------------------X-Inicio-Métodos-Principais-Compressao-X--------------------//
  private int[] realizarCompressao(String entrada) {

    String[] dicionario = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
        "S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "Ç", "Ã", "Á", "Â",
        "É", "Ê", "Í", "Î", "Ó", "Õ", "Ô", "Ú", "Û", " ", "/", ".", "-" };

    String[] compressaoDicionario = new String[dicionario.length * 3];
    System.arraycopy(dicionario, 0, compressaoDicionario, 0, dicionario.length);
    int[] valorCompressao = new int[500];
    valorCompressao = inicializarVetor(valorCompressao);
    int contadorValorCompressao = 0;
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

          valorCompressao[contadorValorCompressao] = saveValorRetorno;
          contadorValorCompressao++;

        }

        int posicaoNoDicionario = qtdElementosString(compressaoDicionario);
        if (!(letraParaSalvarnoDic.equals(""))) {
          compressaoDicionario[posicaoNoDicionario] = letraParaSalvarnoDic;
        }

        proxLetra = "";
        letraS = "";
        letraParaSalvarnoDic = "";
      } else {
        podeEncerrar = true;
        valorCompressao = inicializarVetor(valorCompressao);
      }

    }

    int[] vRetorno = { -1 };

    if (!vetorEstaVazio(valorCompressao)) {

      int qtdElementos = qtdElementosInt(valorCompressao);
      vRetorno = new int[qtdElementos];

      System.arraycopy(valorCompressao, 0, vRetorno, 0, qtdElementos);

    }

    return vRetorno;
  }

  private byte[] toByteArray(short iD, String lapide, String nome, String cnpj,
      String cidade, byte pj, byte pts, int[] nomeArray, int[] cnpjArray, int[] cidadeArray) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);

    // como ta escrevendo a compressao tamanhodoarquivodedados+id,lapide,boolean se
    // for true o nome é um int, se for false é string, nome, boolean verificador
    // pra cnpj, cnpj, boolean verificador cidade, cidade, pj e pontos

    // tamanhoArq+id+lapide+boolean(SETIVERBA)+byte-10(correspondenteaofinaldoarray),
    // tamanhoArq+id+lapide+boolean+nome(SenaoTiverBA),

    fut ft = new fut();

    byte[] ba;
    boolean saveNomeArray = false;
    boolean saveCnpjArray = false;
    boolean saveCidadeArray = false;

    if (!vetorEstaVazio(nomeArray)) {
      saveNomeArray = true;
    }
    boolean podeSetarCnpj = ft.verificaCPNJ(cnpj);

    if (!vetorEstaVazio(cnpjArray) && podeSetarCnpj) {
      saveCnpjArray = true;
    }

    if (!vetorEstaVazio(cidadeArray)) {
      saveCidadeArray = true;
    }

    dos.writeShort(iD);
    dos.writeUTF(lapide);
    if (saveNomeArray) {
      ba = deIntParaByteArray(nomeArray);
      dos.writeBoolean(saveNomeArray);
      dos.write(ba);
      dos.writeByte(-10);
    } else {
      dos.writeBoolean(saveNomeArray);
      dos.writeUTF(nome);
    }

    if (saveCnpjArray) {
      dos.writeBoolean(saveCnpjArray);
      ba = deIntParaByteArray(cnpjArray);
      dos.writeShort(ba.length);
      dos.write(ba);
      dos.writeByte(-10);
    } else {
      dos.writeBoolean(saveCnpjArray);
      dos.writeUTF("");
    }

    if (saveCidadeArray) {
      dos.writeBoolean(saveCidadeArray);
      ba = deIntParaByteArray(cidadeArray);
      dos.write(ba);
      dos.writeByte(-10);
    } else {
      dos.writeBoolean(saveCidadeArray);
      dos.writeUTF(cidade);
    }

    dos.writeByte(pj);
    dos.writeByte(pts);
    return baos.toByteArray();
  }

  private int porcentagemDeCompressao(byte[] depoisCompre, long antesCompre) {

    float a = depoisCompre.length;
    float b = (float) antesCompre;

    a *= 4;
    b *= 4;

    float resp = a / b;

    resp *= 100;
    int respO = (int) resp;
    respO = 100 - respO;

    if (respO <= 0) {
      respO = -1;
    }

    return respO;
  }

  public void compressaoLzw(int entrada) {

    // tem que cromprimir nome, cnpj, cidade
    // ARRAYDEBYTE(ID+LAPIDE+NOME+CNPJ+CIDADE+PARTIDASJOGADAS+PONTOS)modelo de cada
    // registro no arquivo principal.

    // variaveis do método realizar compressao

    RandomAccessFile arqPrinci;
    RandomAccessFile arqCompri;
    RandomAccessFile arqLista;
    RandomAccessFile arqIndice;
    try {
      String caminhodoArqCompri = "src/database/compressão/futebolCompressao" + entrada + ".db";
      String caminhodoArqLista = "src/database/compressão/ListaCompressao" + entrada + ".db";
      String caminhoArqIndice = "src/database/compressão/IndiceCompressao" + entrada + ".db";
      deletaTudo(caminhodoArqCompri);
      arqPrinci = new RandomAccessFile("src/database/futebol.db", "r");
      arqCompri = new RandomAccessFile(caminhodoArqCompri, "rw");
      arqLista = new RandomAccessFile(caminhodoArqLista, "rw");
      arqIndice = new RandomAccessFile(caminhoArqIndice, "rw");
      deletaTudo(caminhodoArqCompri);
      deletaTudo(caminhodoArqLista);
      deletaTudo(caminhoArqIndice);
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

        Short iDclube = arqPrinci.readShort();
        String lapide = arqPrinci.readUTF();
        String ftNome = arqPrinci.readUTF().toUpperCase();
        String ftCnpj = arqPrinci.readUTF().toUpperCase();
        String ftCidade = arqPrinci.readUTF().toUpperCase();
        int[] receberByteComprimidoNome = realizarCompressao(ftNome);
        int[] receberByteComprimidoCnpj = realizarCompressao(ftCnpj);
        int[] receberByteComprimidoCidade = realizarCompressao(ftCidade);

        byte partidasJogadas = arqPrinci.readByte();
        byte pontos = arqPrinci.readByte();

        byte b[];

        b = toByteArray(iDclube, lapide, ftNome, ftCnpj, ftCidade, partidasJogadas, pontos,
            receberByteComprimidoNome, receberByteComprimidoCnpj, receberByteComprimidoCidade);

        // fazer metodo para calcular taxa de compressao
        int taxaCompre = porcentagemDeCompressao(b, arqPrinci.length());

        if (taxaCompre != -1) {
          System.out.println("A taxa de compresao total do arquivo foi de " + taxaCompre + "%");
        } else {
          System.out.println("A compressao resultou numa piora no tamanho do arquivo original NESTE CASO !");
        }

        byte[] pegarLista = Files.readAllBytes(Paths.get("src/database/listainvertida.db"));
        byte[] pegarIndice = Files.readAllBytes(Paths.get("src/database/aindices.db"));

        arqLista.write(pegarLista);
        arqIndice.write(pegarIndice);

        arqCompri.writeInt(tamanhodoArquivo4bytesI);
        arqCompri.write(b);// criar o tobytearray

      }
      arqIndice.close();
      arqLista.close();
      arqCompri.close();
      arqPrinci.close();
    } catch (Exception e) {
      System.out.println("Erro no compressaoLze: " + e.getMessage());
    }

  }
  // --------------------X-Fim-Métodos-Principais-Compressao-X--------------------//

  // --------------------X-Inicio-Métodos-Principais-Descompressao-X--------------------//
}
