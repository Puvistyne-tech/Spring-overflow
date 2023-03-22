const input = document.getElementById("body");
const boldButton = document.getElementById("bold-button");
const italicButton = document.getElementById("italic-button");
const linkButton = document.getElementById("link-button");
//const imageButton = document.getElementById("image-button");
const listButton = document.getElementById("list-button");
const codeButton = document.getElementById("code-button");
const codeBlockButton = document.getElementById("code-block-button");

boldButton.addEventListener("click", () => {
    const start = input.selectionStart;
    const end = input.selectionEnd;
    const selectedText = input.value.substring(start, end);
    let newText;
    if (selectedText) {
        newText = `<b>${selectedText}</b>`;
    } else {
        newText = "<b>insert your text here</b>";
        input.selectionStart = start + 2;
        input.selectionEnd = start + 12;
    }
    input.value = input.value.substring(0, start) + newText + input.value.substring(end);
});

italicButton.addEventListener("click", () => {
    const start = input.selectionStart;
    const end = input.selectionEnd;
    const selectedText = input.value.substring(start, end);
    let newText;
    if (selectedText) {
        newText = `<i>${selectedText}</i>`;
    } else {
        newText = "<i>insert your text here</i>";
        input.selectionStart = start + 2;
        input.selectionEnd = start + 12;
    }
    input.value = input.value.substring(0, start) + newText + input.value.substring(end);
});
codeButton.addEventListener("click", () => {
    const start = input.selectionStart;
    const end = input.selectionEnd;
    const selectedText = input.value.substring(start, end);
    let newText;
    if (selectedText) {
        newText = `<code class="language-markup">${selectedText}</code>`;
    } else {
        newText = "<code class='language-markup'>insert your code here</code>";
        input.selectionStart = start + 2;
        input.selectionEnd = start + 12;
    }
    input.value = input.value.substring(0, start) + newText + input.value.substring(end);
});
codeBlockButton.addEventListener("click", () => {
    const start = input.selectionStart;
    const end = input.selectionEnd;
    const selectedText = input.value.substring(start, end);
    let newText;
    if (selectedText) {
        newText = `<pre><code class="language-markup">${selectedText}</code></pre>`;
    } else {
        newText = "<pre><code class='language-markup'>insert your code here</code></pre>";
        input.selectionStart = start + 2;
        input.selectionEnd = start + 12;
    }
    input.value = input.value.substring(0, start) + newText + input.value.substring(end);
});

linkButton.addEventListener("click", () => {
    const start = input.selectionStart;
    const end = input.selectionEnd;
    const selectedText = input.value.substring(start, end);
    let newText;
    if (selectedText) {
        newText = `[${selectedText}](url)`;
    } else {
        newText = "[insert your text here](url)";
        input.selectionStart = start + 2;
        input.selectionEnd = start + 12;
    }
    input.value = input.value.substring(0, start) + newText + input.value.substring(end);
});

/*imageButton.addEventListener("click", () => {
const start = input.selectionStart;
const end = input.selectionEnd;
const selectedText = input.value.substring(start, end);
const newText = `![${selectedText}](image-url)`;
input.value = input.value.substring(0, start) + newText + input.value.substring(end);
});*/
listButton.addEventListener("click", () => {
    const start = input.selectionStart;
    const end = input.selectionEnd;
    const selectedText = input.value.substring(start, end);
    const newText = `- ${selectedText}`;
    input.value = input.value.substring(0, start) + newText + input.value.substring(end);
});

// const preview = document.getElementById("preview");
//
// const converter = new showdown.Converter();
//
// input.addEventListener("input", () => {
//     preview.innerHTML = converter.makeHtml(input.value);
// });