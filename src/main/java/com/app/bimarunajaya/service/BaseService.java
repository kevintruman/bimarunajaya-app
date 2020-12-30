package com.app.bimarunajaya.service;

import com.app.bimarunajaya.entity.PackageEntity;
import com.app.bimarunajaya.model.CbFilter;
import lombok.extern.slf4j.Slf4j;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
public class BaseService {

    public static final String pathQR = "/tmp/com.app.bimarunajaya/";
    public static final String pathQRAddr = "/tmp/com.app.bimarunajaya/address/";

    private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static SecureRandom rnd = new SecureRandom();

    protected String randomString(int len){
        StringBuilder sb = new StringBuilder(len);
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }

    protected String generateQRCode(String code, String path, String name) {
        ByteArrayOutputStream bous =
                QRCode.from(code)
                        .withSize(250, 250)
                        .to(ImageType.PNG)
                        .stream();

        try {
            Files.createDirectories(Paths.get(path));
            path = path + name + ".PNG";
            OutputStream out = new FileOutputStream(path);
            bous.writeTo(out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e){
            log.error(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return path;
    }

    public String getPathCode(String code, String name) {
        String path = pathQRAddr + name + ".PNG";
        boolean g = Files.notExists(Paths.get(path));
        if (g) return generateQRCode(code, pathQRAddr, name);
        return path;
    }

    public List<CbFilter> listBulan() {
        return Arrays.asList(
                new CbFilter(1, "Januari"),
                new CbFilter(2, "Februari"),
                new CbFilter(3, "Maret"),
                new CbFilter(4, "April"),
                new CbFilter(5, "Mei"),
                new CbFilter(6, "Juni"),
                new CbFilter(7, "Juli"),
                new CbFilter(8, "Agustus"),
                new CbFilter(9, "September"),
                new CbFilter(10, "Oktober"),
                new CbFilter(11, "November"),
                new CbFilter(12, "Desember")
        );
    }

    public List<CbFilter> listTahun() {
        return Arrays.asList(
                new CbFilter(2019, "2019"),
                new CbFilter(2020, "2020"),
                new CbFilter(2021, "2021"),
                new CbFilter(2022, "2022"),
                new CbFilter(2023, "2023")
        );
    }

    public String generateExcelService(List<PackageEntity> l, String bulan, String tahun) throws IOException {
        ExcelFunctionService efs = new ExcelFunctionService();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("laporan-" + bulan + "-" + tahun);

        Cell laporan = efs.createCell(sheet, 1, 1, null, null, null);
        laporan.setCellValue("Laporan " + bulan + " " + tahun);

        Cell tJumlahTransaksi = efs.createCell(sheet, 2, 1, null, null, null);
        tJumlahTransaksi.setCellValue("Jumlah Transaksi");
        Cell jumlahTransaksi = efs.createCell(sheet, 2, 2, null, null, null);
        jumlahTransaksi.setCellValue(l.size());

        Cell tTotalBiaya = efs.createCell(sheet, 3, 1, null, null, null);
        tTotalBiaya.setCellValue("Total Biaya");
        Cell totalBiaya = efs.createCell(sheet, 3, 2, null, null, null);
        totalBiaya.setCellValue("Rp. " + l.stream().mapToDouble(PackageEntity::getPrice).sum());

        Cell b0 = efs.createCell(sheet, 5, 1, null, null, null);
        b0.setCellValue("Tanggal Buat");
        Cell b1 = efs.createCell(sheet, 5, 2, null, null, null);
        b1.setCellValue("No. Resi");

        Cell b2 = efs.createCell(sheet, 5, 3, null, null, null);
        b2.setCellValue("Alamat Pengirim");
        Cell b3 = efs.createCell(sheet, 5, 4, null, null, null);
        b3.setCellValue("Nama Pengirim");
        Cell b4 = efs.createCell(sheet, 5, 5, null, null, null);
        b4.setCellValue("Telepon Pengirim");

        Cell b5 = efs.createCell(sheet, 5, 6, null, null, null);
        b5.setCellValue("Alamat Penerima");
        Cell b6 = efs.createCell(sheet, 5, 7, null, null, null);
        b6.setCellValue("Nama Penerima");
        Cell b7 = efs.createCell(sheet, 5, 8, null, null, null);
        b7.setCellValue("Telepon Penerima");
        Cell b8 = efs.createCell(sheet, 5, 9, null, null, null);
        b8.setCellValue("Pulau");
        Cell b9 = efs.createCell(sheet, 5, 10, null, null, null);
        b9.setCellValue("Kota");

        Cell b10 = efs.createCell(sheet, 5, 11, null, null, null);
        b10.setCellValue("Deskripsi Paket");
        Cell b11 = efs.createCell(sheet, 5, 12, null, null, null);
        b11.setCellValue("Berat");
        Cell b12 = efs.createCell(sheet, 5, 13, null, null, null);
        b12.setCellValue("Dimensi");

        Cell b13 = efs.createCell(sheet, 5, 14, null, null, null);
        b13.setCellValue("Biaya Kirim");
        Cell b14 = efs.createCell(sheet, 5, 15, null, null, null);
        b14.setCellValue("Keterangan Biaya Kirim");

        Cell b15 = efs.createCell(sheet, 5, 16, null, null, null);
        b15.setCellValue("Dibuat Oleh");
        Cell b16 = efs.createCell(sheet, 5, 17, null, null, null);
        b16.setCellValue("Lokasi");

        SimpleDateFormat sdff = new SimpleDateFormat("dd MMMM yyyy");

        int startRowParam = 6;
        for (PackageEntity param : l) {
            Cell c0 = efs.createCell(sheet, startRowParam, 1, null, null, null);
            c0.setCellValue(sdff.format(param.getCreateDate()));
            Cell c1 = efs.createCell(sheet, startRowParam, 2, null, null, null);
            c1.setCellValue(param.getResiNumber());

            Cell c2 = efs.createCell(sheet, startRowParam, 3, null, null, null);
            c2.setCellValue(param.getAddressFrom());
            Cell c3 = efs.createCell(sheet, startRowParam, 4, null, null, null);
            c3.setCellValue(param.getSendName());
            Cell c4 = efs.createCell(sheet, startRowParam, 5, null, null, null);
            c4.setCellValue(param.getSendPhone());

            Cell c5 = efs.createCell(sheet, startRowParam, 6, null, null, null);
            c5.setCellValue(param.getAddressTo());
            Cell c6 = efs.createCell(sheet, startRowParam, 7, null, null, null);
            c6.setCellValue(param.getReceiveName());
            Cell c7 = efs.createCell(sheet, startRowParam, 8, null, null, null);
            c7.setCellValue(param.getReceivePhone());
            Cell c8 = efs.createCell(sheet, startRowParam, 9, null, null, null);
            c8.setCellValue(param.getKota().getParent().getName().toUpperCase());
            Cell c9 = efs.createCell(sheet, startRowParam, 10, null, null, null);
            c9.setCellValue(param.getKota().getName().toUpperCase());

            Cell c10 = efs.createCell(sheet, startRowParam, 11, null, null, null);
            c10.setCellValue(param.getPackageDescription());
            Cell c11 = efs.createCell(sheet, startRowParam, 12, null, null, null);
            c11.setCellValue(param.getWeight() + " kg");
            Cell c12 = efs.createCell(sheet, startRowParam, 13, null, null, null);
            c12.setCellValue(param.getDimension().getName().toUpperCase());

            Cell c13 = efs.createCell(sheet, startRowParam, 14, null, null, null);
            c13.setCellValue("Rp. " + param.getPrice());
            Cell c14 = efs.createCell(sheet, startRowParam, 15, null, null, null);
            c14.setCellValue(param.getPriceDescription());

            Cell c15 = efs.createCell(sheet, startRowParam, 16, null, null, null);
            c15.setCellValue(param.getCreateBy().getFullName());
            Cell c16 = efs.createCell(sheet, startRowParam, 17, null, null, null);
            c16.setCellValue(param.getStationBy().getFullName());

            startRowParam += 1;
        }

        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Files.createDirectories(Paths.get(pathQR));

        String n = "laporan-" + sdf.format(now) + ".xlsx";
        String fileLocation = pathQR + n;
        FileOutputStream outputStream = new FileOutputStream(fileLocation);
        workbook.write(outputStream);
        workbook.close();
        return n;
    }

}
