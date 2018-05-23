package io.boonlogic.soul.land.microservices.registration.config

import com.google.protobuf.ExtensionRegistry
import com.google.protobuf.Message
import com.google.protobuf.util.JsonFormat
import org.springframework.cloud.stream.binder.OriginalContentTypeResolver
import org.springframework.http.converter.protobuf.ExtensionRegistryInitializer
import org.springframework.messaging.converter.AbstractMessageConverter
import org.springframework.messaging.converter.ContentTypeResolver
import org.springframework.messaging.converter.MessageConversionException
import org.springframework.util.ClassUtils
import org.springframework.util.MimeType
import org.springframework.util.MimeTypeUtils.TEXT_PLAIN
import org.springframework.util.MimeTypeUtils.APPLICATION_JSON
import java.io.*
import java.lang.reflect.Method
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.concurrent.ConcurrentHashMap

private val DEFAULT_CHARSET = StandardCharsets.UTF_8
private val PROTOBUF = MimeType("application", "x-protobuf", DEFAULT_CHARSET)
private const val X_PROTOBUF_SCHEMA_HEADER = "X-Protobuf-Schema"
private const val X_PROTOBUF_MESSAGE_HEADER = "X-Protobuf-Message"

class ProtobufMessageConverter @JvmOverloads constructor(
    private val protobufFormatSupport: ProtobufFormatSupport? = when {
        ClassUtils.isPresent("com.google.protobuf.util.JsonFormat",
            ProtobufMessageConverter::class.java.classLoader) -> ProtobufJavaUtilSupport()
        else -> null
    },
    private val registryInitializer: ExtensionRegistryInitializer? = null
    ) : AbstractMessageConverter(PROTOBUF) {

    private val extensionRegistry = ExtensionRegistry.newInstance()

    init {
        supportedMimeTypes.clear()
        supportedMimeTypes += protobufFormatSupport?.supportedMimeTypes()?.toList() ?:
            listOf(PROTOBUF, TEXT_PLAIN)

        registryInitializer?.initializeExtensionRegistry(extensionRegistry)

        contentTypeResolver = OriginalContentTypeResolver()

    }

    override fun supports(clazz: Class<*>?) =
        Message::class.java.isAssignableFrom(clazz)

    override fun getDefaultContentType(payload: Any?) = PROTOBUF


    override fun convertFromInternal(
        message: org.springframework.messaging.Message<*>?,
        targetClass: Class<*>?,
        conversionHint: Any?): Any? {

        var result = null
        try {
            val mimeType =
                contentTypeResolver.resolve(message?.headers) ?:
                (conversionHint as? MimeType) ?: return null

            // byte[] payload = (byte[]) message.getPayload();
            val payload = message?.payload as? Array<Byte>
//            .let {}

//            val builder
//            when {
//                PROTOBUF.isCompatibleWith(mimeType) ->
//                Text
//            }
//             {
//
//            }



        } catch (e: IOException) {
            throw MessageConversionException(message, "Failed to read payload", e)
        }

        return result
    }

    private companion object {

        private val methodCache = ConcurrentHashMap<Class<*>, Method>()

        fun getMessageBuilder(clazz: Class<out Message>): Message.Builder {
            val method = methodCache[clazz] ?:
            clazz.getMethod("newBuilder")

            return method.invoke(clazz) as Message.Builder
        }
    }


}


interface ProtobufFormatSupport {
    fun supportedMimeTypes(): Array<MimeType>

    fun supportsWriteOnly(mediaType: MimeType?): Boolean

    @Throws(IOException::class)
    fun merge(input: InputStream, charset: Charset, contentType: MimeType,
              extensionRegistry: ExtensionRegistry, builder: Message.Builder)

    @Throws(IOException::class)
    fun print(message: Message, output: OutputStream, contentType: MimeType, charset: Charset)
}

class ProtobufJavaUtilSupport(
    private val parser: JsonFormat.Parser = JsonFormat.parser(),
    private val printer: JsonFormat.Printer = JsonFormat.printer()
) : ProtobufFormatSupport {

    override fun supportedMimeTypes(): Array<MimeType> =
        arrayOf(PROTOBUF, TEXT_PLAIN, APPLICATION_JSON)

    override fun supportsWriteOnly(mediaType: MimeType?): Boolean = false

    override fun merge(input: InputStream, charset: Charset,
                       contentType: MimeType, extensionRegistry: ExtensionRegistry,
                       builder: Message.Builder) {

        if(contentType.isCompatibleWith(APPLICATION_JSON)) {
            parser.merge(InputStreamReader(input, charset), builder)
        } else {
            throw IOException("protobuf-java-util does not support $contentType format")
        }
    }

    override fun print(message: Message, output: OutputStream,
                       contentType: MimeType, charset: Charset) {
        if(contentType.isCompatibleWith(APPLICATION_JSON)) {
            val writer = OutputStreamWriter(output, charset)
            printer.appendTo(message, writer)
            writer.flush()
        } else {
            throw IOException("protobuf-java-util does not support $contentType format")
        }
    }
}
