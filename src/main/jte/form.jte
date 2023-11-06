@import com.keevol.keewebx.utils.CsrfTokens

@param io.vertx.core.json.JsonObject pageContext

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=yes" />
    <meta author="fujohnwang" />
    <meta keywords="王福强,fujohnwang" />
    <meta description="" />
    <title>CHANGE</title>
    <style>
        ::-webkit-scrollbar {
            width: 11px;
        }
        ::-webkit-scrollbar-thumb{
            background-color: #3F83F8;
            border-radius: 3px;
        }
        html{
            scroll-behavior: smooth;
        }
        body{
            font-family: source-serif-pro, Georgia, Cambria, "Times New Roman", Times, serif;
        }
    </style>
    <link href="https://cdn.jsdelivr.net/npm/daisyui@3.5.0/dist/full.css" rel="stylesheet" type="text/css" />
    <script src="https://cdn.tailwindcss.com/"></script>
    <script src="https://unpkg.com/htmx.org@1.9.6"></script>
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.0/dist/jquery.min.js"></script>
    <script defer src="https://cdn.jsdelivr.net/npm/alpinejs@3.11.1/dist/cdn.min.js"></script>
    <script defer src="https://cdn.jsdelivr.net/npm/@alpinejs/intersect@3.11.1/dist/cdn.min.js"></script>

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@shoelace-style/shoelace@2.9.0/cdn/themes/light.css" />
    <script type="module" src="https://cdn.jsdelivr.net/npm/@shoelace-style/shoelace@2.9.0/cdn/shoelace-autoloader.js"></script>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/animejs/3.2.1/anime.min.js" integrity="sha512-z4OUqw38qNLpn1libAN9BsoDx6nbNFio5lA6CuTp9NlK83b89hgyCVq+N5FdBJptINztxn1Z3SaKSKUS5UP60Q==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/gsap/3.11.4/gsap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/gsap/3.11.4/ScrollTrigger.min.js"></script>
</head>
<body>
<main class="container mx-auto p-3 space-y-3">

    <section class="body-font relative text-gray-600">
        <form hx-post="/form_submit" hx-target="#resultDisplay">
            <div class="container mx-auto px-5 py-24">

                <div class="mx-auto md:w-2/3 lg:w-1/2">
                    <div class="-m-2 flex flex-wrap">
                        <div class="w-full p-2">
                            <div class="relative">
                                <h1 class="text-6xl">CSRF TOKEN FORM DEMO</h1>
                                <label for="message" class="text-sm leading-7 text-gray-600">Message</label>
                                <textarea id="message" name="message" class="h-32 w-full resize-none rounded border border-gray-300 bg-gray-100 bg-opacity-50 px-3 py-1 text-base leading-6 text-gray-700 outline-none transition-colors duration-200 ease-in-out focus:border-indigo-500 focus:bg-white focus:ring-2 focus:ring-indigo-200"></textarea>
<%--                                <input type="hidden" name="csrf_token" value="${pageContext.getString("csrf_token")}"/>--%>
                                $unsafe{CsrfTokens.hiddenInputFrom(pageContext)}
<%--                                same effect as direct declaration of hidden input but with strong type java code and unescape the output.--%>
                            </div>
                        </div>
                        <div class="w-full p-2">
                            <button class="mx-auto flex rounded border-0 bg-indigo-500 px-8 py-2 text-lg text-white hover:bg-indigo-600 focus:outline-none">Button</button>
                        </div>
                        <div id="resultDisplay"></div>
                    </div>
                </div>
            </div>
        </form>
    </section>
</main>

</body>
</html>
