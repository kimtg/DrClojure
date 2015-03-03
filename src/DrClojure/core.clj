; DrClojure
; (C) 2012-2015 Kim, Taegyoon

(ns DrClojure.core
  (:gen-class)
  (:import
    (javax.swing JFrame SwingUtilities JPanel BoxLayout JMenuBar JMenu JMenuItem)
    (java.awt Button TextArea BorderLayout Font TextField)
    (java.awt.event ActionEvent ActionListener)
    (java.io Writer)))

(defn init []
  (def title "DrClojure")
  
  (def aCurFileName (atom ""))
  
  (. javax.swing.UIManager setLookAndFeel 
    (. javax.swing.UIManager getSystemLookAndFeelClassName)) ; make look native
  
  (def frame (new JFrame))
  (defn update-title []
    (. frame setTitle (str (deref aCurFileName) " - " title)))
  
  (update-title)
  
  (def text (new TextArea 20 80))
  (def text-out (new TextArea 6 80))
  (def textf (new TextField))
  (def button (new Button "Eval"))
      
  (def out
    (proxy [Writer] []
      (close [])
      (flush [])
      (write [thing]
        (. SwingUtilities invokeLater
          (proxy [Runnable] []
            (run [] (. text-out append thing)))))))
  
  (defn eval-code [code]
    (try (binding [*out* out]
           (. clojure.lang.Compiler load (new java.io.StringReader (str "(ns user) " code))))
      (catch Exception e e)))
  
  (. button addActionListener
    (proxy [ActionListener] []
      (actionPerformed [e]
        (. text-out append (str (str (eval-code (. text getText))) \newline)))))
  
  (defn textf-action []
    (def code (. textf getText))
    (. text-out append (str "> " code "\n" (str (eval-code code)) \newline))
    (. textf setText ""))
  
  (. textf addActionListener
    (proxy [ActionListener] []
      (actionPerformed [e]
        (textf-action))))
  
  (def fc (new javax.swing.JFileChooser))
  (. fc setFileFilter (new javax.swing.filechooser.FileNameExtensionFilter "Clojure file (*.clj)" (into-array ["clj"])))
  
  (def panel (new JPanel))
  
  (def menuBar (new JMenuBar))
  (def menuFile (new JMenu "File"))
  (def menuNew (new JMenuItem "New"))
  (def menuOpen (new JMenuItem "Open..."))
  (def menuSave (new JMenuItem "Save"))
  (def menuSaveAs (new JMenuItem "Save As..."))
  (. menuBar add menuFile)
  (. menuFile add menuNew)
  (. menuFile add menuOpen)
  (. menuFile add menuSave)
  (. menuFile add menuSaveAs)
  (. frame setJMenuBar menuBar)
  
  (. menuNew addActionListener
    (proxy [ActionListener] []
      (actionPerformed [e]
        (reset! aCurFileName "")
        (update-title)
        (. text setText ""))))
  
  (. menuOpen addActionListener
    (proxy [ActionListener] []
      (actionPerformed [e]
        (def ret (. fc showOpenDialog nil))
        (when (== ret javax.swing.JFileChooser/APPROVE_OPTION)
          (def fileName (. (. fc getSelectedFile) getCanonicalPath))
          (reset! aCurFileName fileName)
          (update-title) 
          (. text setText (slurp fileName))))))
  
  (defn fileSave [fileName]
    (spit fileName (. text getText)))
  
  (defn fileSaveAs []
    (def ret (. fc showSaveDialog nil))
    (when (== ret javax.swing.JFileChooser/APPROVE_OPTION)
      (def fileName (. (. fc getSelectedFile) getCanonicalPath))
      (reset! aCurFileName fileName)
      (update-title)        
      (fileSave fileName)))
  
  (. menuSave addActionListener
    (proxy [ActionListener] []
      (actionPerformed [e]      
        (if (= (deref aCurFileName) "")
          (fileSaveAs) 
          (fileSave fileName)))))
  
  (. menuSaveAs addActionListener
    (proxy [ActionListener] []
      (actionPerformed [e]
        (fileSaveAs)))))

(defn -main []
  (init)
  (. frame setDefaultCloseOperation JFrame/EXIT_ON_CLOSE)
  (let [font (new Font "Monospaced" Font/PLAIN 12)]
    (. text setFont font)
    (. text-out setFont font)
    (. textf setFont font))
  (. panel setLayout (new BoxLayout panel BoxLayout/PAGE_AXIS))
  (. frame add button BorderLayout/NORTH)
  (. frame add text)
  (. panel add textf)
  (. panel add text-out)
  (. frame add panel BorderLayout/SOUTH)
  (. frame pack)
  (. frame setVisible true)
  (. textf setText "(clojure-version)")
  (textf-action))
