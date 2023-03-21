class MainActivity : AppCompatActivity() {

    private var nfcAdapter: NfcAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        if (nfcAdapter == null) {
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        if (!nfcAdapter!!.isEnabled) {
            Toast.makeText(this, "NFC is disabled.", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "NFC is enabled.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()

        val intent = Intent(this, javaClass).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val intentFilters = arrayOf(IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED), IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED), IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED))

        val techList = arrayOf(arrayOf(NfcA::class.java.name), arrayOf(NfcB::class.java.name), arrayOf(NfcF::class.java.name), arrayOf(NfcV::class.java.name), arrayOf(IsoDep::class.java.name), arrayOf(MifareClassic::class.java.name), arrayOf(MifareUltralight::class.java.name), arrayOf(Ndef::class.java.name), arrayOf(NdefFormatable::class.java.name))

        nfcAdapter!!.enableForegroundDispatch(this, pendingIntent, intentFilters, techList)
    }

    override fun onPause() {
        super.onPause()

        nfcAdapter!!.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        if (intent.action == NfcAdapter.ACTION_TAG_DISCOVERED) {
            val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            val id = tag?.id
            Toast.makeText(this, "Tag ID: ${ByteArrayToHexString(id)}", Toast.LENGTH_LONG).show()
        }
    }

    private fun ByteArrayToHexString(inarray: ByteArray?): String {
        var i: Int
        var j = 0
        val hex = StringBuilder(inarray!!.size * 2)
        while (j < inarray.size) {
            i = inarray[j].toInt() and 0xff
            if (i <= 0xf) hex.append('0')
            hex.append(Integer.toHexString(i))
            ++j
        }
        return hex.toString()
    }
}
