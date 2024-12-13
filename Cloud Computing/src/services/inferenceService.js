const tfjs = require('@tensorflow/tfjs-node');

const predict = async (image, model) => {
    const tensor = tfjs.node
        .decodeJpeg(image)
        .resizeNearestNeighbor([224, 224])
        .expandDims()
        .toFloat();

    const prediction = model.predict(tensor);
    const score = await prediction.data();
    const confidenceScore = Math.max(...score) * 100;

    const label = confidenceScore <= 50 ? 'Non-cancer' : 'Cancer';
    let result;
    let suggestion;

    if (label === 'Cancer') {
        result = "Melanoma";
        suggestion = "<h1>Solusi untuk Melanoma</h1><p>Jika Anda atau orang terdekat terdiagnosis melanoma, berikut langkah-langkah yang dapat dilakukan:</p><ol><li><b>Pengobatan Medis:</b></li><ul><li><b>Operasi</b>: Dokter akan mengangkat melanoma bersama jaringan sehat di sekitarnya.</li><li><b>Terapi Imun</b>: Obat seperti pembrolizumab membantu meningkatkan kekebalan tubuh untuk melawan kanker.</li><li><b>Terapi Target</b>: Jika melanoma Anda memiliki mutasi gen tertentu, dokter dapat memberikan obat seperti vemurafenib.</li><li><b>Kemoterapi atau Radiasi</b>: : Digunakan jika melanoma telah menyebar ke organ lain.</li></ul><li><b>Pemeriksaan Rutin</b></li><p>Pastikan untuk melakukan check-up secara berkala dengan dokter kulit untuk memantau kondisi Anda.</p></ol>"
    }

    if (label === 'Non-cancer') {
        result = "Normal";
        suggestion = "<h1>Pencegahan Melanoma</h1><p>Untuk melindungi diri dari melanoma, Anda dapat mengambil langkah berikut:</p><ol><li><b>Lindungi Kulit Anda</b></li><ul><li><b>Gunakan tabir surya (SPF 30+)</b>: Oleskan sebelum keluar rumah, dan ulangi setiap 2 jam jika berada di bawah sinar matahari.</li><li><b>Pakai pakaian pelindung</b>: Gunakan topi lebar, kacamata hitam, dan pakaian panjang saat keluar rumah.</li><li><b>Hindari sinar UV</b>: Batasi waktu di bawah matahari antara pukul 10.00 - 16.00.</li></ul><li><b>Pantau Perubahan Kulit</b></li><ul><li>Periksa kulit Anda secara rutin</li><li>Gunakan metode <b>ABCDE</b> untuk mendeteksi tahi lalat mencurigakan:</li><ul><li><b>A (Asimetris)</b>: Bentuk tahi lalat tidak rata.</li><li><b>B (Batas)</b>: Tepi tahi lalat tidak rata atau kabur.</li><li><b>C (Warna)</b>: Warna tidak merata atau berubah.</li><li><b>D (Diameter)</b>: Lebih besar dari 6 mm.</li><li><b>E (Evolusi)</b>: Tahi lalat berubah ukuran, bentuk, atau warna.</li></ul></ul><li><b>Hindari Risiko Lain</b></li><ul><li>Jangan gunakan tanning bed.</li><li>Jaga pola makan sehat dan hindari merokok untuk meningkatkan kekebalan tubuh.</li></ul><li><b>Deteksi Dini</b></li><p>Jika Anda memiliki riwayat keluarga melanoma, segera konsultasi dengan dokter kulit untuk pencegahan lebih lanjut.</p></ol>"
    }

    return { confidenceScore, label, result, suggestion };
}

module.exports = predict;