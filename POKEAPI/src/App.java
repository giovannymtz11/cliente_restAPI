// Giovanny Martinez Dominguez
// 24/febrero/2024

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.json.JSONObject;

public class App {
    private static final String API_ENDPOINT = "https://pokeapi.co/api/v2/pokemon/";

    public static void main(String[] args) {
        JFrame frame = createFrame();
        JButton newButton = new JButton("Nuevo");

        // Maneja el evento del botón "Nuevo"
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Solicita al usuario el número del Pokémon
                    String pokemonNumber = JOptionPane.showInputDialog(frame, "Ingresa el número del Pokémon:");
                    if (pokemonNumber != null) {
                        int pokemonId = Integer.parseInt(pokemonNumber);
                        handlePokemonRequest(frame, pokemonId);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Número de Pokémon inválido.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error al obtener información del Pokémon.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        frame.add(newButton, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JFrame createFrame() {
        JFrame frame = new JFrame("Pokémon Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        return frame;
    }

    private static void handlePokemonRequest(JFrame frame, int pokemonId) {
        try {
            String pokemonInfo = getPokemonInfo(pokemonId);
            String pokemonName = extractPokemonName(pokemonInfo);
            String imageUrl = extractFrontDefaultImageURL(pokemonInfo);
            String ability = extractAbility(pokemonInfo);
            int baseExperience = extractBaseExperience(pokemonInfo);

            JPanel pokemonPanel = createPokemonPanel(pokemonName, ability, baseExperience, imageUrl);
            frame.getContentPane().removeAll(); // Limpia el contenido anterior
            frame.add(pokemonPanel, BorderLayout.CENTER);
            frame.add(createNewButton(frame), BorderLayout.SOUTH); 
            frame.pack();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error al obtener información del Pokémon.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error al procesar la información del Pokémon.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private static JButton createNewButton(JFrame frame) {
        JButton newButton = new JButton("Nuevo");

        // Maneja el evento del botón "Nuevo"
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Solicita al usuario el número del Pokémon
                    String pokemonNumber = JOptionPane.showInputDialog(frame, "Ingresa el número del Pokémon:");
                    if (pokemonNumber != null) {
                        int pokemonId = Integer.parseInt(pokemonNumber);
                        handlePokemonRequest(frame, pokemonId);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Número de Pokémon inválido.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error al obtener información del Pokémon.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        return newButton;
    }

    private static String getPokemonInfo(int pokemonId) throws Exception {
        URL url = new URL(API_ENDPOINT + pokemonId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Leer la respuesta
        Scanner scanner = new Scanner(connection.getInputStream());
        StringBuilder response = new StringBuilder();
        while (scanner.hasNext()) {
            response.append(scanner.nextLine());
        }
        scanner.close();

        return response.toString();
    }

    private static String extractPokemonName(String pokemonInfo) {
        JSONObject json = new JSONObject(pokemonInfo);
        return json.getString("name");
    }

    private static String extractFrontDefaultImageURL(String pokemonInfo) {
        JSONObject json = new JSONObject(pokemonInfo);
        JSONObject sprites = json.getJSONObject("sprites");
        return sprites.getString("front_default");
    }

    private static String extractAbility(String pokemonInfo) {
        JSONObject json = new JSONObject(pokemonInfo);
        JSONObject abilities = json.getJSONArray("abilities").getJSONObject(0);
        return abilities.getJSONObject("ability").getString("name");
    }

    private static int extractBaseExperience(String pokemonInfo) {
        JSONObject json = new JSONObject(pokemonInfo);
        return json.getInt("base_experience");
    }

    private static JPanel createPokemonPanel(String name, String ability, int baseExperience, String imageUrl)
            throws IOException {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        ImageIcon imageIcon = new ImageIcon(new URL(imageUrl));
        Image image = imageIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(image));

        JLabel nameLabel = new JLabel("Nombre del Pokémon: " + name);
        JLabel abilityLabel = new JLabel("Habilidad: " + ability);
        JLabel experienceLabel = new JLabel("Experiencia Base: " + baseExperience);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS)); // Layout vertical

        infoPanel.add(nameLabel);
        infoPanel.add(abilityLabel);
        infoPanel.add(experienceLabel);
                
        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(imageLabel, BorderLayout.SOUTH);

        return panel;
    }

}
